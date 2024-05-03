package org.unibl.etf.rest.client;

import java.util.ArrayList;
import java.util.List;

public class Proizvodi {
    public static List<Proizvod> list;
 
    public Proizvodi() {
    	list = new ArrayList<>();
	}
    public List<Proizvod> getList() {
        return list;
    }
 
    public void setList(List<Proizvod> list) {
        this.list = list;
    }
}