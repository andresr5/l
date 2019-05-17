package com.movii.gateway.utils;

import java.io.InputStream;
import java.io.SequenceInputStream;

public class InputStreamCollector {
	  private InputStream is;

	  public void collectInputStream(InputStream is) {
		  System.out.println("IN collect input stream");
		  System.out.println(is==null);
		  System.out.println(is);
	    if (this.is == null) this.is = is;
	    this.is = new SequenceInputStream(this.is, is);
	  }

	  public InputStream getInputStream() {
	    return this.is;
	  }
	}