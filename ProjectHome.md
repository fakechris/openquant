# "Hack the Market" #

# Intro #
OpenQuant is an open source backtesting and quantitative analysis platform for time series financial data. It offers a simple tradesystem development framework using open financial data from Yahoo.

# Quick Start #
Included with the library is a simple command line interface for executing backtests.  Follow these steps to set up your own backtesting environment:

1. Download the most recent version of OpenQuant from the [downloads](http://code.google.com/p/openquant/downloads/list) section.

2. Unzip to desired directory and start up the OpenQuant command interface by executing openquant.sh (linux / unix) or openquant.bat on windows.  To get a list of the available commands type ?list or ?l

3. Load up a desired dataset.  A data set is a list of stocks with a specific startdate.  So to load up a few tech stocks from 2005 to today execute the following command :

**openquant> clist mylist 2005-01-01 ATVI ADBE AKAM ALTR AMZN AMGN APOL AAPL AMAT ADSK**

Note that these stock lists are persisted to the filesystem, so you will be able to reuse them when you shutdown and restart the openquant command line tool.

4. Create a backtest script in the scripts directory.  All backtest scripts in the OpenQuant command line tool are written in the [groovy programming language](http://groovy.codehaus.org) for interoperability with Java and ease of use.  Here is snippet of backtest code:

```
def Close = context.closeSeries()
def EMAClose = Close.EMA(12)
def RSI = Close.RSI(25)

for (int bar = 25; bar < context.barsCount() - 1; bar++) {
	
	println "Date[${context.date(bar)}], Close[${Close.getAt(bar)}], " +
	"EMA[${EMAClose.getAt(bar)}], RSI[${RSI.getAt(bar)}]"
	
	Position pos = context.getLastOpenPosition();	
	
	if (RSI.getAt(bar - 1) > RSI.getAt(bar - 2)) {
		context.buyAtLimit(bar, context.close(bar), 1000, "ELL")
	}
	else{
		if (context.hasOpenPositions()) {
			context.sellAtLimit(bar, pos, pos.getEntryPrice() * 1.05, "XLL")
		}
	
		if (context.hasOpenPositions()) {
			context.sellAtStop(bar, pos, pos.getEntryPrice() * 0.95, "XLS")
		}
	}	
}
```

4. Run a backtest found in the scripts directory.  OpenQuant comes with a test.groovy tradesystem that you can edit and then execute from the openquant console.  Note the .groovy extension is unnecessary when specifying the tradesystem.

**openquant> test mylist test**

5. Open up the resulting test.jpg equity chart to see how the tradesystem did.  ( Not so good in this case, but you get the idea :) )  The blue line represents the total equity and the red line indicates the available cash.  When you see something like this, you are having a good day:

![http://i.imgur.com/hTiDx.jpg](http://i.imgur.com/hTiDx.jpg)