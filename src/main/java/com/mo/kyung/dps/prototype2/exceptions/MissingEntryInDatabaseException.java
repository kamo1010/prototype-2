package com.mo.kyung.dps.prototype2.exceptions;

public class MissingEntryInDatabaseException extends Exception {

	/**
	 * thrown when trying to access an entry (user, topic or message) 
	 * which does not exist in Database
	 */
	private static final long serialVersionUID = 3507641348132330525L;

	public MissingEntryInDatabaseException(String message) {
		super(message);
	}
	

}
