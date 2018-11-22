package com.autokey.aok_vin;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import static android.R.attr.name;

public class WebService {

	private static String NAMESPACE = "http://tempuri.org/";
	private static String URL = "http://192.168.10.1/AutokeysWS/Service.asmx";
	//private static String URL = "http://192.168.0.12/AutokeysWS/Service.asmx";
	//private static String URL = "http://www.stands4.com/services/v2/quotes.php?uid=1001&tokenid=tk324324324&searchtype=AUTHOR&query=Albert+Einstein";
	//private static String URL = "http://www.webservicex.net/NAICS.asmx";
	private static String SOAP_ACTION = "http://tempuri.org/";

	public static String invokeHelloWorldWS(String aTelaio, String webMethName) {
		String resTxt = null;
		// Create request
		SoapObject request = new SoapObject(NAMESPACE, webMethName);
		// Property which holds input parameters
		PropertyInfo celsiusPI = new PropertyInfo();
		// Set Name
		celsiusPI.setName("name");
		// Set Value
		celsiusPI.setValue(name);
		// Set dataType
		celsiusPI.setType(String.class);
		// Add the property to request object
		request.addProperty(celsiusPI);
		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);


		try {
			// Invole web service
			androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to fahren static variable

			resTxt = response.toString();
            if(resTxt != "1" )
                resTxt = "VIN Already Inserted !";
            else
                resTxt = "OK !";

		} catch (Exception e) {
			e.printStackTrace();
			resTxt = "Error occured :" + e.getMessage();

		}

		return resTxt;
	}

	public static String invokeCercaTelaio4Android(String aTelaio)
	{
		String resTxt = null;
		SoapObject request = new SoapObject(NAMESPACE, "CercaTelaio4Android");
		// Property which holds input parameters
		PropertyInfo myTelaio = new PropertyInfo();
		// Set Name
		myTelaio.setName("aTelaio");
		// Set Value
		myTelaio.setValue(aTelaio);
		// Set dataType
		myTelaio.setType(String.class);
		// Add the property to request object
		request.addProperty(myTelaio);


		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		//Set envelope as dotNet
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		try {
			// Invoke web service
			androidHttpTransport.call(SOAP_ACTION+"CercaTelaio4Android", envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to resTxt variable static variable
			resTxt = response.toString();

		} catch (Exception e) {
			//Print error
			e.printStackTrace();
			//Assign error message to resTxt
			resTxt = "Error occured";
		}
		//Return resTxt to calling object
		return resTxt;
	}

	public static String invokeInsertTelaio4Android(String aTelaio, String aModello, String aOperator)
	{
		String resTxt = null;
		SoapObject request = new SoapObject(NAMESPACE, "InsertTelaio4Android");

		// Property which holds input parameters
		// Da qui innizializzo i parametri,
		// in questo caso 3 parametri -> 3 Properties
		PropertyInfo myTelaio = new PropertyInfo();
		// Set Name
		myTelaio.setName("aTelaio");
		// Set Value
		myTelaio.setValue(aTelaio);
		// Set dataType
		myTelaio.setType(String.class);
		// Add the property to request object
		request.addProperty(myTelaio);

		PropertyInfo myModello = new PropertyInfo();
		// Set Name
		myModello.setName("aModello");
		// Set Value
		myModello.setValue(aModello);
		// Set dataType
		myModello.setType(String.class);
		// Add the property to request object
		request.addProperty(myModello);

		PropertyInfo myOperator = new PropertyInfo();
		// Set Name
		myOperator.setName("aOperator");
		// Set Value
		myOperator.setValue(aOperator);
		// Set dataType
		myOperator.setType(String.class);
		// Add the property to request object
		request.addProperty(myOperator);

		//endregion

		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		//Set envelope as dotNet
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
		try {
			// Invoke web service
			androidHttpTransport.call(SOAP_ACTION+"InsertTelaio4Android", envelope);
			// Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			// Assign it to resTxt variable static variable
			resTxt = response.toString();

		} catch (Exception e) {
			//Print error
			e.printStackTrace();
			//Assign error message to resTxt
			resTxt = "Error occured";
		}
		//Return resTxt to calling object
		return resTxt;
	}
}
