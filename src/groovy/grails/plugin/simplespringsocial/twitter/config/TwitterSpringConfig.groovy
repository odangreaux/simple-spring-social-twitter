package grails.plugin.simplespringsocial.twitter.config

import javax.inject.Inject
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionFactory
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.support.ConnectionFactoryRegistry
import org.springframework.social.twitter.api.impl.TwitterTemplate
import org.springframework.social.twitter.connect.TwitterConnectionFactory
import org.springframework.social.twitter.api.Twitter

@Configuration
class TwitterSpringConfig {
	@Inject
	ConnectionFactoryRegistry connectionFactoryRegistry
	@Inject
	ConnectionRepository connectionRepository

	@Bean
	ConnectionFactory twitterConnectionFactory() {
		println "Configuring SimpleSpringSocialTwitter"

		// gets required parameters
		String consumerKey = ConfigurationHolder.config.grails.plugin.simplespringsocial.twitter.consumerKey
		String consumerSecret = ConfigurationHolder.config.grails.plugin.simplespringsocial.twitter.consumerSecret

		// checks the parameters
		assert consumerKey, "You must configure the Twitter consumerKey into Config.groovy with the following parameter : grails.plugin.simplespringsocial.twitter.consumerKey"
		assert consumerSecret, "You must configure the Twitter consumerSecret into Config.groovy with the following parameter : grails.plugin.simplespringsocial.twitter.consumerSecret"

		ConnectionFactory twitterConnectionFactory  = new TwitterConnectionFactory(consumerKey, consumerSecret)
		connectionFactoryRegistry.addConnectionFactory(twitterConnectionFactory)

		return twitterConnectionFactory
	}

	@Bean
	@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
	public Twitter twitter() {
		Connection<Twitter> connection = connectionRepository.findPrimaryConnection(Twitter)
		connection != null ? connection.getApi() : new TwitterTemplate()
	}
}
