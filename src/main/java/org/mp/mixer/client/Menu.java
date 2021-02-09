package org.mp.mixer.client;


import org.mp.mixer.util.Keyin;
import org.mp.mixer.domain.Address;
import org.mp.mixer.dto.MixRequestDTO;
import org.mp.mixer.services.JobCoinService;
import org.mp.mixer.services.MixerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public void mainMenu() throws IOException, InterruptedException {
        // Local variable
        int inputValue = 1;

        while (inputValue != 3) {
            log.info("=========================================");
            System.out.println("|   Jobcoin Mixer                       |");
            System.out.println("=========================================");
            System.out.println("| Options:                              |");
            System.out.println("|        1. Mix                         |");
            System.out.println("|        2. View Transactions           |");
            System.out.println("|        3. Exit                        |");
            System.out.println("=========================================");
            inputValue = Keyin.inInt(" Select option: ");


            switch (inputValue) {
                case 1:
                    System.out.println("Mix selected");
                    int mixInputValue;
                    List<MixRequestDTO> mixes = new ArrayList<>();
                    do {
                        mixMenu(mixes);
                        System.out.println("1.  Add another request");
                        System.out.println("2.  Process request(s)");
                        mixInputValue = Keyin.inInt(" Select option: ");
                    } while (mixInputValue != 2);
                    MixerService mixerService = new MixerService();
                    mixerService.processRequest(mixes);

                    break;
                case 2:
                    System.out.println("View all transactions");
                    JobCoinService jcs = new JobCoinService();
                    jcs.printAllTransactions();
                    break;
                case 3:
                    System.out.println("Exit ");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid selection");

            }

        }

    }

    public List<MixRequestDTO> mixMenu(List<MixRequestDTO> mixes) {
        System.out.println("=========================================");
        System.out.println("|   Jobcoin Mixer                       |");
        System.out.println("=========================================");
        String addressValue = Keyin.inString("Enter empty addresses separated by comma.");
        double jobcoinValue = Keyin.inDouble("How jobcoins to mix?");
        int mixTimeValue = Keyin.inInt("How many seconds to mix?");

        String[] tokens = addressValue.split(",");
        List<Address> addressesList = new ArrayList<>();
        for (String tok : tokens) {
            addressesList.add(new Address(tok));
        }
        mixes.add(new MixRequestDTO(addressesList, (float) jobcoinValue, mixTimeValue));
        return mixes;
    }


}
