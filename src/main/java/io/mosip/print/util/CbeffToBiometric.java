package io.mosip.print.util;

import io.mosip.kernel.biometrics.constant.BiometricType;
import io.mosip.kernel.biometrics.entities.BIR;
import io.mosip.kernel.biometrics.spi.CbeffUtil;
import io.mosip.kernel.cbeffutil.impl.CbeffImpl;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The Class CbeffToBiometricUtil.
 * 
 * @author  Dhanendra
 */
@Component
public class CbeffToBiometric {

	/** The print logger. */
	private static final Logger logger = LoggerFactory.getLogger(CbeffToBiometricUtil.class);


	/** The cbeffutil. */
	private CbeffUtil cbeffutil = new CbeffImpl();

	/**
	 * Instantiates biometric util
	 *
	 */
	public CbeffToBiometric(CbeffUtil cbeffutil) {

	}

	/**
	 * Gets the BIR.
	 *
	 * @param cbeffFileString the cbeff file string
	 * @param type            the type
	 * @param subType         the sub type
	 * @return the photo
	 * @throws Exception the exception
	 */
	public BIR getBIR(String cbeffFileString, String type, List<String> subType) throws Exception {
		logger.debug("CbeffToBiometricUtil::getImageBytes()::entry");
		BIR bir = null;
		if (cbeffFileString != null) {
			List<BIR> bIRTypeList = null;
			try {
				bIRTypeList = getBIRTypeList(cbeffFileString);
				bir = getBIRByTypeAndSubType(bIRTypeList, type, subType);
			} catch (Exception e) {
				throw e;
			}
		}
		logger.debug("CbeffToBiometricUtil::getImageBytes()::exit");
		return bir;
	}

	/**
	 * Gets the photo by type and sub type.
	 *
	 * @param type        the type
	 * @param subType     the sub type
	 * @return the photo by type and sub type
	 */
	private BIR getBIRByTypeAndSubType(List<BIR> bIRList, String type, List<String> subType) {
		for (BIR bir : bIRList) {
			if (bir.getBdbInfo() != null) {
				List<BiometricType> singleTypeList = bir.getBdbInfo().getType();
				List<String> subTypeList = bir.getBdbInfo().getSubtype();

				boolean isType = isBiometricType(type, singleTypeList);
				boolean isSubType = isSubType(subType, subTypeList);

				if (isType && isSubType) {
					return bir;
				}
			}
		}
		return null;
	}

// pass the bir direclty || face bir to serialize to string
	/**
	 * Checks if is sub type.
	 *
	 * @param subType     the sub type
	 * @param subTypeList the sub type list
	 * @return true, if is sub type
	 */
	private boolean isSubType(List<String> subType, List<String> subTypeList) {
		return subTypeList.equals(subType) ? Boolean.TRUE : Boolean.FALSE;
	}

	private boolean isBiometricType(String type, List<BiometricType> biometricTypeList) {
		boolean isType = false;
		for (BiometricType biometricType : biometricTypeList) {
			if (biometricType.value().equalsIgnoreCase(type)) {
				isType = true;
			}
		}
		return isType;
	}

	/**
	 * Gets the BIR type list.
	 *
	 * @param cbeffFileString the cbeff file string
	 * @return the BIR type list
	 * @throws Exception the exception
	 */

	public List<BIR> getBIRTypeList(String cbeffFileString) throws Exception {
		return cbeffutil.getBIRDataFromXML(Base64.decodeBase64(cbeffFileString));
	}

	/**
	 * Gets the BIR type list.
	 *
	 * @param xmlBytes byte array of XML data
	 * @return the BIR type list
	 * @throws Exception the exception
	 */
	public List<BIR> getBIRDataFromXML(byte[] xmlBytes) throws Exception {
		return cbeffutil.getBIRDataFromXML(xmlBytes);
	}
}
