package com.nju.edu.erp.service;

import java.util.List;

public interface BackUpService {

    void backUp(String fileName);

    void load(String fileName);

    List<String> getNames();
}
