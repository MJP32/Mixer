/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mp.mixer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

	private final String from;
	private final String to;
	private final float amount;

	public Transaction(@JsonProperty("from") String from, @JsonProperty("to") String to, @JsonProperty("amount") float amount) {
		this.from = from;
		this.to = to;
		this.amount = amount;
	}
	public String getTo() {
		return to;
	}
	public String getFrom() {
		return this.from;
	}
	public float getAmount() {
		return this.amount;
	}
	@Override
	public String toString() {
		return "Transaction{" +
				"from='" + from + '\'' +
				", to='" + to + '\'' +
				", amount=" + amount +
				'}';
	}

}
