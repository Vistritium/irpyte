package com.irpyte.server.form;

import lombok.Data;

@Data
public class SettingsForm {

    private String search;

    public String getSearch() {
        return search;
    }

}
