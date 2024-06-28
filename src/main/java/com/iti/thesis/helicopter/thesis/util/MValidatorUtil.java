package com.iti.thesis.helicopter.thesis.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iti.thesis.helicopter.thesis.core.collection.MData;
import com.iti.thesis.helicopter.thesis.core.collection.MMultiData;
import com.iti.thesis.helicopter.thesis.core.exception.MException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MValidatorUtil {
	
	private final Logger log = LoggerFactory.getLogger(MValidatorUtil.class);
	
	public void validate( MData ipParam, String... sField ) throws MException {
		for ( String sKey : sField ) {
			if ( StringUtil.isEmpty( StringUtil.trim( ipParam.getString( sKey ) ) ) ) {
				log.debug( String.format( ">>> %s cannot be empty", sKey ) );
				throw new MException( "000001", sKey + " cannot be empty" );
			}
		}
	}
	
	public MData emptyToBeNull( MData ipParam, String... sField ) {
		String sTemp = null;
		for ( String sKey : sField ) {
			sTemp = ipParam.getString( sKey );
			if ( StringUtil.isEmpty( sTemp ) ) {
				ipParam.set( sKey, null );
			}
		}
		return ipParam;
	}
	
	public MData nullToBeEmpty( MData ipParam, String... sField ) {
		String sTemp = null;
		for ( String sKey : sField ) {
			sTemp = ipParam.getString( sKey );
			if ( StringUtil.isEmpty( sTemp ) ) {
				ipParam.set( sKey, "" );
			}
		}
		return ipParam;
	}
	
	/*
	* Check condition if empty do not append into StringBuilder, escape check duplicate too many empty
	*/
	public void findDuplicated( MMultiData grid01, String... fields ) throws MException {
		final Set<String> set = new HashSet<>();
		for ( MData mData : grid01.toListMData() ) {
			StringBuilder sb = new StringBuilder();
			for ( String field : fields ) {
				String value = mData.getString( field );
				if( StringUtil.isNotBlank( value ) ) {
					sb.append( value );
				}
			}
			if ( sb.length() > 0 && !set.add( sb.toString() ) ) {
				throw new MException( "000001", String.format( "There are duplicated field %s in the grid01.", Arrays.asList( fields ) ) );
			}
			
		}
	}
	
	public void findDifferential( MMultiData grid01, String field ) throws MException {
		final Set<String> set = new HashSet<>();
		for ( MData mData : grid01.toListMData() ) {
			if ( set.add( mData.getString( field ) ) && set.size() > 1 ) {
					throw new MException( "000001", String.format( "There are different field of %s in the grid01", field ) );
			}
		}
	}
	
	public MMultiData removeDuplicated( MMultiData grid01, String... fields ) {
		final Set<String> set = new HashSet<>();
		MMultiData outputData = new MMultiData();
		for ( MData mData : grid01.toListMData() ) {
			StringBuilder sb = new StringBuilder();
			for ( String field : fields ) {
				String value = mData.getString( field );
				sb.append( value );
			}
			if ( !set.add( sb.toString() ) ) {
				continue;
			}
			outputData.addMData( mData );
		}
		return outputData;
	}
	
	public MData checkKeysForUpdate( MData inputData, MData tobeUpdate ) throws MException {
		
		MData outputData = new MData();
		Set<String> keySet = inputData.keySet();
		Iterator<String> iterator = keySet.iterator();
		
		while( iterator.hasNext() ) {
			
			String key = iterator.next();
			if ( !StringUtil.isBlank( StringUtil.trim( inputData.getString( key ) ) )
					&& tobeUpdate.containsKey( key ) ) {
				outputData.set( key, inputData.get( key ) );
			}
		}
			
		return outputData; 
	}
	
	public void validateFields( MData ipParam, MData ipMsg ) throws MException {
		for ( String sKey : ipMsg.keySet() ) {
			if ( StringUtil.isBlank( StringUtil.trim( ipParam.getString( sKey ) ) ) ) {
				log.debug( ipMsg.getString( sKey ) );
				throw new MException( "000001", ipMsg.getString( sKey ) );
			}
		}
	} 
	
	public void validateZero( MData ipParam, MData ipMsg ) throws MException {
		for ( String sKey : ipMsg.keySet() ) {
			if ( ipParam.getBigDecimal( sKey ).compareTo( BigDecimal.ZERO ) <= 0 ) {
				log.debug(ipMsg.getString( sKey ));
				throw new MException( "000001", ipMsg.getString( sKey ) );
			}
		}
	}
	
	
	public <T extends Enum<T>> void validateCode(Class<T> enumClass, String value) {
		for (T code : enumClass.getEnumConstants()) {
			if (code.toString().equals(value)) {
				return;
			}
		}
		throw new MException( "000001", value + " not exist!" );
	}
}
