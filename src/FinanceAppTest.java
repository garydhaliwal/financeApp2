import java.text.DecimalFormat;

import android.util.Log;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;


public class FinanceAppTest extends UiAutomatorTestCase {
	
	//Declaring UI elements
	UiObject stockAppIcon = new UiObject(new UiSelector().text("Stocks"));
	UiObject searchIcon = new UiObject(new UiSelector().resourceId("org.dayup.stocks:id/menu_insert"));
	UiObject searchBar = new UiObject(new UiSelector().resourceId("org.dayup.stocks:id/search_input"));		
	UiObject searchResult = new UiObject(new UiSelector().resourceId("org.dayup.stocks:id/stock_item"));

	UiObject currentPrice = new UiObject(new UiSelector().resourceId("org.dayup.stocks:id/details_lastTradedPrice"));		
	UiObject yearLow = new UiObject(new UiSelector().resourceId("org.dayup.stocks:id/details_yearRangeText_low"));		
	UiObject yearHigh = new UiObject(new UiSelector().resourceId("org.dayup.stocks:id/details_yearRangeText_high"));		
	UiObject eps = new UiObject(new UiSelector().resourceId("org.dayup.stocks:id/details_epsText"));		
	UiObject back1 = new UiObject(new UiSelector().className("android.widget.ImageButton"));		
	UiObject back2 = new UiObject(new UiSelector().resourceId("org.dayup.stocks:id/back"));		
	
	DecimalFormat df = new DecimalFormat("#.00");
	
	
	public void testStockData() throws UiObjectNotFoundException {
		
		//Navigate to the home screen and load Finance app
		getUiDevice().pressHome();
		stockAppIcon.click();

		
		//Retrieve WTW stock data
		double[] wtwStockData = retrieveStockData("WTW");
		
		
		//Output percentage comparisons to console
		Log.d("Output", "Todays price of $" + wtwStockData[0] +
				" is " + (int) Math.abs(((wtwStockData[0] - wtwStockData[2])/wtwStockData[2] * 100)) + "% lower than the 52 week high " +
						"and is " + (int) ((wtwStockData[0] - wtwStockData[1])/wtwStockData[1] * 100) + "% higher than the 52 week low");

		
		//Retrieve WDAY stock data
		double[] wdayStockData = retrieveStockData("WDAY");

		
		//Output higher EPS between WTW and WDAY
		Log.d("Output", "The higher EPS is " + Math.max(wtwStockData[3], wdayStockData[3]));
		
	}
	
	private double[] retrieveStockData(String symbol) throws UiObjectNotFoundException{
		
		double[] stockData = new double[4];
		
		//Search stock symbol and load details
		searchIcon.click();
		searchBar.setText(symbol);			
		getUiDevice().pressEnter();
		searchResult.waitForExists(1000);
		searchResult.click();
		
		
		//Store stock data
		stockData[0] = Double.parseDouble(currentPrice.getText());
		stockData[1] = Double.parseDouble(yearLow.getText());
		stockData[2] = Double.parseDouble(yearHigh.getText());
		stockData[3] = Double.parseDouble(eps.getText());

		back1.clickAndWaitForNewWindow();
		back2.click();

		
		return stockData;
	}
	
}
