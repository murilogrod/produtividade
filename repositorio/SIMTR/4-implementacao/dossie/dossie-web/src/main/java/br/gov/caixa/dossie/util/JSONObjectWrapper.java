package br.gov.caixa.dossie.util;

import org.json.JSONObject;

/**
 * @author SIOGP
 */
public class JSONObjectWrapper extends JSONObject {

	/**
	 * @param data
	 */
	public JSONObjectWrapper(String data){
		super(data);
	}

	@Override
	public String getString(String key){
		if (super.has(key)) {
			return super.getString(key);
		} else {
			return null;
		}
	}

	
	@Override
	public boolean getBoolean(String key){
		if (super.has(key)) {
		return super.getBoolean(key);
		} else {
			return false;
		}
	}
}
