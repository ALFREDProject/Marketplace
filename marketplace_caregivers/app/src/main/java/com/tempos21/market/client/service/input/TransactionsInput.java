package com.tempos21.market.client.service.input;

import com.tempos21.market.client.bean.Transactions;

public class TransactionsInput implements Input {

    private Transactions change;

    public Transactions getTransactions() {
        return change;
    }

    public void setTransactions(Transactions transactions) {
        this.change = transactions;
    }


}
