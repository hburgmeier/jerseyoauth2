package com.burgmeier.jerseyoauth2.authsrv.openid;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;

public class OpenIdConsumer {

	private ConsumerManager manager;

//Google=https://www.google.com/accounts/o8/id Yahoo=http://yahoo.com/ 
	@SuppressWarnings("unchecked")
	public void authRequest(String openidServiceId, String returnToUrl, HttpServletRequest httpReq, HttpServletResponse httpResp) throws IOException, ServletException {
		try {
			// --- Forward proxy setup (only if needed) ---
			// ProxyProperties proxyProps = new ProxyProperties();
			// proxyProps.setProxyName("proxy.example.com");
			// proxyProps.setProxyPort(8080);
			// HttpClientFactory.setProxyProperties(proxyProps);

			// perform discovery on the user-supplied identifier
			List<?> discoveries = manager.discover(openidServiceId);

			// attempt to associate with the OpenID provider
			// and retrieve one service endpoint for authentication
			DiscoveryInformation discovered = manager.associate(discoveries);

			// store the discovery information in the user's session
			httpReq.getSession().setAttribute(OpenIdConstants.OPENID_DISC, discovered);

			// obtain a AuthRequest message to be sent to the OpenID provider
			AuthRequest authReq = manager.authenticate(discovered, returnToUrl);

			// Attribute Exchange example: fetching the 'email' attribute
			FetchRequest fetch = FetchRequest.createFetchRequest();
			fetch.addAttribute("email",
			// attribute alias
					"http://schema.openid.net/contact/email", // type URI
					true); // required

			// attach the extension to the authentication request
			authReq.addExtension(fetch);

			if (!discovered.isVersion2()) {
				// Option 1: GET HTTP-redirect to the OpenID Provider endpoint
				// The only method supported in OpenID 1.x
				// redirect-URL usually limited ~2048 bytes
				httpResp.sendRedirect(authReq.getDestinationUrl(true));
			} else {
				// Option 2: HTML FORM Redirection (Allows payloads >2048 bytes)

				sendFormRedirect(httpResp, authReq.getDestinationUrl(false), (Map<String,String>)authReq.getParameterMap());
			}
		} catch (OpenIDException e) {
			e.printStackTrace(System.err);
		}

	}

	public Identifier verifyResponse(HttpServletRequest httpReq) {
		try {
			// extract the parameters from the authentication response
			// (which comes in as a HTTP request from the OpenID provider)
			ParameterList response = new ParameterList(httpReq.getParameterMap());

			// retrieve the previously stored discovery information
			DiscoveryInformation discovered = (DiscoveryInformation) httpReq.getSession().getAttribute(OpenIdConstants.OPENID_DISC);

			// extract the receiving URL from the HTTP request
			StringBuffer receivingURL = httpReq.getRequestURL();
			String queryString = httpReq.getQueryString();
			if (queryString != null && queryString.length() > 0)
				receivingURL.append("?").append(httpReq.getQueryString());

			// verify the response; ConsumerManager needs to be the same
			// (static) instance used to place the authentication request
			VerificationResult verification = manager.verify(receivingURL.toString(), response, discovered);

			// examine the verification result and extract the verified
			// identifier
			Identifier verified = verification.getVerifiedId();
			if (verified != null) {
				AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();

				if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
					FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);

					List<?> emails = fetchResp.getAttributeValues("email");
					String email = (String) emails.get(0);
					
					httpReq.getSession().setAttribute(OpenIdConstants.OPENID_SESSION_VAR, new OpenIDUser(email));
				}

				return verified; // success
			}
		} catch (OpenIDException e) {
			// present error to the user
		}

		return null;
	}
	
	protected void sendFormRedirect(HttpServletResponse httpResp, String endpoint, Map<String, String> parameterMap) throws IOException
	{
		ServletOutputStream out = httpResp.getOutputStream();
        
		httpResp.setContentType("text/html; charset=UTF-8");
		httpResp.addHeader( "pragma", "no-cache" );
		httpResp.addHeader( "Cache-Control", "no-cache" );

        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<head>");
		out.println("    <title>OpenID HTML FORM Redirection</title>");
		out.println("</head>");
		out.println("<body onload=\"document.forms['openid-form-redirection'].submit();\">");
		out.println("    <form name=\"openid-form-redirection\" action=\""+endpoint+"\" method=\"post\" accept-charset=\"utf-8\">");

		for (Entry<String, String> entry : parameterMap.entrySet()) {
			out.println("	<input type=\"hidden\" name=\""+entry.getKey()+"\" value=\""+entry.getValue()+"\"/>");
		}
		
		out.println("        <button type=\"submit\">Continue...</button>");
		out.println("    </form>");
		out.println("</body>");
		out.println("</html>");
		
		out.flush();		
	}

}
