package com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor;

import java.util.EnumSet;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;

public class CombinedExtractor implements IExtractor {

	protected final EnumSet<ParameterStyle> styles;
	protected final String field;

	public CombinedExtractor(String field) {
		super();
		this.field = field;
		this.styles = EnumSet.allOf(ParameterStyle.class);
	}	
	
	public CombinedExtractor(String field, EnumSet<ParameterStyle> styles) {
		super();
		this.field = field;
		this.styles = styles;
	}

	@Override
	public String extractValue(IHttpRequest request) {
		String value = null;
		for (ParameterStyle style : styles)
		{
			IExtractor extractor = getExtractor(field, style);
			value = extractor.extractValue(request);
			if (StringUtils.isNotEmpty(value))
				break;
		}
		return value;
	}
	
	protected IExtractor getExtractor(String field, ParameterStyle parameterStyle)
	{
		switch (parameterStyle)
		{
		case HEADER:
			return new HeaderExtractor(field);
		case QUERY:
			return new QueryParameterExtractor(field);
		case BODY:
			return new FormExtractor(field);
		}
		throw new IllegalArgumentException(parameterStyle.toString());		
	}

}
