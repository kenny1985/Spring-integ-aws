/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.aws.xml;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractOutboundChannelAdapterParser;
import org.w3c.dom.Element;

/**
 * The parser for the {@code <int-aws:sns-outbound-channel-adapter>}.
 *
 * @author Artem Bilan
 */
public class SnsOutboundChannelAdapterParser extends AbstractOutboundChannelAdapterParser {

	@Override
	protected AbstractBeanDefinition parseConsumer(Element element, ParserContext parserContext) {
		AbstractBeanDefinition beanDefinition =
				new SnsOutboundGatewayParser()
						.parseHandler(element, parserContext)
						.getBeanDefinition();
		beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, false);
		return beanDefinition;
	}

}
