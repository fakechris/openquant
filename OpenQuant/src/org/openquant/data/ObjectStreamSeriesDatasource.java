package org.openquant.data;

/*
Copyright (c) 2011, Jay Logelin
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following 
conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
in the documentation and/or other materials provided with the distribution.  Neither the name of the JQuant nor the names of its 
contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Set;

import org.openquant.backtest.CandleSeries;

public class ObjectStreamSeriesDatasource implements SeriesDatasource {

	private String filename;

	private SeriesCollection seriesCollection = new SeriesCollection();

	public ObjectStreamSeriesDatasource(final String filename) {
		this.filename = filename;

		if(exists()){
			// initialize if exists
			FileInputStream fis = null;
			ObjectInputStream in = null;
			try {
				fis = new FileInputStream(filename);
				in = new ObjectInputStream(fis);
				seriesCollection = (SeriesCollection) in.readObject();
				in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public Date getBeginDate() {
		return seriesCollection.getBeginDate();
	}

	public Date getEndDate() {
		return seriesCollection.getEndDate();
	}

	public Set<String> getSymbols(){
		return seriesCollection.getSymbols();		
	}
	
	public boolean exists(){
		return new File(filename).exists();
	}

	public void insertCandleSeries(CandleSeries series) {
		seriesCollection.addSeries(series);
	}

	public void commit() {
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(seriesCollection);
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@Override
	public CandleSeries fetchSeries(String symbol) throws Exception {
		return seriesCollection.getSeries(symbol);
	}

}
