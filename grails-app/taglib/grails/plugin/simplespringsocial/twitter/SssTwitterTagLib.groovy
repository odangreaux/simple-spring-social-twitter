package grails.plugin.simplespringsocial.twitter

class SssTwitterTagLib {
	static namespace = "sss"

	/**
	 * Creates a link to connect to Twitter.
	 * The content of this tag is reused as the content of the generated <a> tag.
	 *
	 * @attr id The id of the link
	 * @attr id The name of the link
	 */
	def twitterConnectLink = {attrs, body ->
		out << "<a href='"+createLink(controller: "sssTwitter", action: "login") + "'"
		if(attrs.id) out << " id='$attrs.id'"
		if(attrs.name) out << " name='$attrs.name'"
		out << ">"
		out << body()
		out << "</a>"
	}

}
