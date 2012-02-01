package grails.plugin.simplespringsocial

import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionRepository
import org.springframework.social.connect.support.ConnectionFactoryRegistry
import org.springframework.social.oauth1.AuthorizedRequestToken
import org.springframework.social.oauth1.OAuth1Operations
import org.springframework.social.oauth1.OAuth1Parameters
import org.springframework.social.oauth1.OAuthToken
import org.springframework.social.twitter.api.Twitter
import org.springframework.social.twitter.connect.TwitterConnectionFactory

/**
 * Controller for Twitter related operations.
 */
class SssTwitterController {
	ConnectionFactoryRegistry connectionFactoryRegistry
	ConnectionRepository connectionRepository

	def index() {
		redirect(action: "login")
	}

	/**
	 * Login action
	 */
	def login = {
		// Gets a OAuth1Operations object
		TwitterConnectionFactory connectionFactory = (TwitterConnectionFactory) connectionFactoryRegistry.getConnectionFactory("twitter");
		OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();

		// builds the oauth1 authorize URL
		OAuthToken requestToken = oauthOperations.fetchRequestToken(createLink(action: "connected", absolute: true).toString(), null);
		session["ssstwitter.oauthtoken"] = requestToken
		String authorizeUrl = oauthOperations.buildAuthenticateUrl(requestToken.getValue(), OAuth1Parameters.NONE);

		// redirects to the oauth1 authorize URL
		redirect(url: authorizeUrl)
	}

	/**
	 * Callback action called by the oauth1 provider
	 */
	def connected = {
		// gets the accessToken and the verifier from the callback URL
		def verifier = params.oauth_verifier

		// creates a Spring social Connection object
		TwitterConnectionFactory connectionFactory = (TwitterConnectionFactory) connectionFactoryRegistry.getConnectionFactory("twitter");
		OAuth1Operations oauthOperations = connectionFactory.getOAuthOperations();
		OAuthToken oAuthToken = oauthOperations.exchangeForAccessToken(new AuthorizedRequestToken(session["ssstwitter.oauthtoken"], verifier), null);
		Connection<Twitter> connection = connectionFactory.createConnection(oAuthToken);

		// registers the connection into the connectionRepository
		connectionRepository.addConnection(connection)

		// redirects to the user defined controller and action
		def redirectController = grailsApplication.config.grails.plugin.simplespringsocial.twitter.loginSuccess.controller
		def redirectAction = grailsApplication.config.grails.plugin.simplespringsocial.twitter.loginSuccess.action
		redirect(controller: redirectController, action: redirectAction)
	}
}
