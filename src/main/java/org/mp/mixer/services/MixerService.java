package org.mp.mixer.services;

import org.mp.mixer.domain.Address;
import org.mp.mixer.domain.HouseWallet;
import org.mp.mixer.domain.Transaction;
import org.mp.mixer.dto.MixRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MixerService {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
    @Value("${house.name}")
    String houseName;
    GenerateAddress generateAddress = new GenerateAddress();
    JobCoinService jcs = new JobCoinService();
    public static void main(String[] args) throws InterruptedException {
        List<MixRequestDTO> mixes= new ArrayList<>();
        mixes.add(new MixRequestDTO(Arrays.asList(new Address("addr1"), new Address("addr2"), new Address("addr3")),50f, 10));
        mixes.add(new MixRequestDTO(Arrays.asList(new Address("addr4"), new Address("addr5"), new Address("addr6")),100f, 20));
        new MixerService().processRequest(mixes).subscribe(System.out::println);
    }

    interface MyEventListener<T> {
        void onDataChunk(List<MixRequestDTO> chunk);
        void processComplete();
    }

    public Flux<Long> processRequest(List<MixRequestDTO> mixes) throws InterruptedException {
        class EventProcessor {
            public MyEventListener l;
            public void register(MyEventListener listener) {
                this.l = listener;
            }
            public void process() throws InterruptedException {
                log.info("processing");
                mixes.forEach(i -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            l.onDataChunk(mixes);
                        });
                Thread.sleep(2000);
                l.processComplete();
            }
        }

        EventProcessor processor = new EventProcessor();
        Address houseAddress = new Address(new GenerateAddress().getHashedAddress("houseName"));
        HouseWallet houseWallet = new HouseWallet(houseAddress, 0f);
        // Create the FLUX from the Event listener

        Flux<Long> interval = Flux.interval(Duration.ofSeconds(mixes.get(0).getTime()));
        interval.subscribe();
        Flux<String> proccessMix =
                Flux.create(sink -> processor.register(
                        new MyEventListener<List<MixRequestDTO>>() {
                            public void onDataChunk(List<MixRequestDTO> chunk) {
                                for (MixRequestDTO request : chunk) {
                                    if (request.getAmount() > 0) {
                                        log.info("Transfer from wallet to house account");
                                        float total = houseWallet.getAmount() + request.getAmount();
                                        houseWallet.setAmount(total);
                                        log.info("Mix");
                                        doMix(request, houseWallet);
                                    }
                                    sink.next(request.toString());
                                }
                            }
                            public void processComplete() {
                                sink.complete();
                            }
                        }));

        proccessMix.subscribe(
                System.out::println,
                null,
                () -> log.info("House is Waiting...."));
        processor.process();
        return Flux.zip(interval,proccessMix).map(Tuple2::getT1);
    }

    public int doMix(MixRequestDTO request, HouseWallet houseWallet) {
        log.info("Doing Mix");
        List<Address> addressList = request.getAddressList();
        Random rand = new Random();
        int numAddresses = addressList.size();
        float total = houseWallet.getAmount();
        float min = .001f;
        List<Float> listRandom = new ArrayList<>();
        while(total > 1) {
            float max = total;
            float randAmount = min + rand.nextFloat() * (max - min);
            randAmount = randAmount / numAddresses + 1;
            total -=randAmount;
            listRandom.add(randAmount);
            System.out.println("Total is " + total);
        }

        listRandom.add(total);

        List<Transaction> mixedTransactions = new ArrayList<>();
        for (float f : listRandom) {
            for (Address name : addressList) {
                String hashedAddress = generateAddress.getHashedAddress(name.toString());
                mixedTransactions.add(new Transaction(houseWallet.getAddress().toString(), hashedAddress, f));
                log.info(new Transaction(houseWallet.getAddress().toString(), hashedAddress, f).toString());
            }
        }

        jcs.createTransaction(mixedTransactions);
        return mixedTransactions.size();
    }
}
