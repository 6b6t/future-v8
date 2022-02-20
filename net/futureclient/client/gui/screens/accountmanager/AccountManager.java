/*
 * Decompiled with CFR 0.152.
 */
package com.gitlab.nuf.exeter.gui.screens.accountmanager;

import com.gitlab.nuf.api.registry.ListRegistry;
import com.gitlab.nuf.exeter.config.Config;
import com.gitlab.nuf.exeter.gui.screens.accountmanager.Account;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public final class AccountManager
extends ListRegistry<Account> {
    public AccountManager() {
        this.registry = new ArrayList();
        new Config("accounts.txt"){

            @Override
            public void load(Object ... source) {
                try {
                    String readLine;
                    if (!this.getFile().exists()) {
                        this.getFile().createNewFile();
                    }
                    BufferedReader br2 = new BufferedReader(new FileReader(this.getFile()));
                    AccountManager.this.getRegistry().clear();
                    while ((readLine = br2.readLine()) != null) {
                        try {
                            String[] split = readLine.split(":");
                            AccountManager.this.register(new Account(split[0], split[1]));
                        }
                        catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    br2.close();
                }
                catch (Exception e3) {
                    e3.printStackTrace();
                }
            }

            @Override
            public void save(Object ... destination) {
                try {
                    if (!this.getFile().exists()) {
                        this.getFile().createNewFile();
                    }
                    BufferedWriter bw2 = new BufferedWriter(new FileWriter(this.getFile()));
                    for (Account account : AccountManager.this.getRegistry()) {
                        bw2.write(account.getLabel() + ":" + account.getPassword());
                        bw2.newLine();
                    }
                    bw2.close();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        };
    }
}

