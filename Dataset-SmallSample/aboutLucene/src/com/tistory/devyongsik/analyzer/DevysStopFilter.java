package com.tistory.devyongsik.analyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

/**
 * @author need4spd, need4spd@cplanet.co.kr, 2011. 7. 8.
 *
 */
public class DevysStopFilter extends TokenFilter {
	private Log logger = LogFactory.getLog(DevysStopFilter.class);
	private boolean ENABLE_POSITION_INCREMENTS_DEFAULT = false;
	private boolean enablePositionIncrements = ENABLE_POSITION_INCREMENTS_DEFAULT;

	private CharTermAttribute charTermAtt;
	private PositionIncrementAttribute posIncrAtt;

	private List<String> stopWords = new ArrayList<String>();

	private void initStopWord() {
		stopWords.add("the");
		stopWords.add(".");
	}
	
	public DevysStopFilter(TokenStream input) {
		super(input);
		initStopWord();
		charTermAtt = addAttribute(CharTermAttribute.class);
		posIncrAtt = addAttribute(PositionIncrementAttribute.class);

		if(logger.isDebugEnabled())
			logger.debug("initailize...");
	}

	public void setEnablePositionIncrements(boolean enable) {
		this.enablePositionIncrements = enable;
	}

	public boolean getEnablePositionIncrements() {
		return enablePositionIncrements;
	}

	public void setEnablePositionIncrementsDefault(boolean defaultValue) {
		ENABLE_POSITION_INCREMENTS_DEFAULT = defaultValue;
	}

	public boolean getEnablePositionIncrementsDefault() {
		return ENABLE_POSITION_INCREMENTS_DEFAULT;
	}

	@Override
	public boolean incrementToken() throws IOException {

		if(logger.isDebugEnabled())
			logger.debug("incrementToken");


		// return the first non-stop word found
		int skippedPositions = 0;

		//다음 Token을 가져와서 불용어 사전에 등록된 단어이면 loop를 돌면서 그 다음 Token을 다시 읽어오고
		//사전에 등록되지 않은 단어라면 다음 필터에게 return 시켜준다.
		while(input.incrementToken()) {

			if(logger.isDebugEnabled())
				logger.debug("원래 리턴 될 TermAtt : " + charTermAtt.toString() + "stopWordDic.isExist : " + stopWords.contains(charTermAtt.toString()));

			if(!stopWords.contains(charTermAtt.toString())) {
				if(enablePositionIncrements) {
					posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement() + skippedPositions);
				}

				return true;
			}

			skippedPositions += posIncrAtt.getPositionIncrement();
		}
		if(logger.isDebugEnabled())
			logger.debug("return null");

		return false;
	}
}
