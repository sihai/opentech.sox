/*
 * openteach inc.
 * sox
 */
package com.openteach.sox.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.openteach.sox.SoxHttpContext;
import com.openteach.sox.SoxRequest;
import com.openteach.sox.SoxResponse;
import com.openteach.sox.common.SoxConstants;
import com.openteach.sox.common.configuration.SessionAttributeConfiguration;
import com.openteach.sox.common.configuration.SessionConfiguration;
import com.openteach.sox.common.configuration.SystemAttribute;
import com.openteach.sox.common.configuration.annotation.AnnontationConfigParser;
import com.openteach.sox.common.storage.StorageType;
import com.openteach.sox.crypter.EncrypterAlgorithm;
import com.openteach.sox.crypter.EncrypterFactory;
import com.openteach.sox.session.SingletonSessionManagerFactory;
import com.openteach.sox.session.SoxSession;

/**
 * SOX入口Filter
 * @author sihai
 *
 */
public class SoxFilter extends AbstractFilter {
	
	private static final String PARAMETER_DOMAIN = "domain";
	private static final String PARAMETER_SCAN_PACKAGES = "scan_packages";
	private static final String PARAMETER_STORAGE_TYPE = "storage_type";
	private static final String PARAMETER_TIMEOUT = "timeout";
	private static final String PARAMETER_ENCRYPTER_ALGORITHM = "encrtypter_algorithm";
	private static final String PARAMETER_PRIVATE_KEY = "private_key";

	private SessionConfiguration sc;
	
	@Override
	protected void initialize() throws ServletException {
		
		int timeout = SoxConstants.DEFAULT_LIFE_CYCLE;
		String str = this.getInitParameter(PARAMETER_TIMEOUT, "");
		if(StringUtils.isNotBlank(str)) {
			try {
				timeout = Integer.valueOf(str);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("sox timeout must not be integer");
			}
		}
		
		// 扫描注解
		str = this.getInitParameter(PARAMETER_SCAN_PACKAGES, null);
		if(StringUtils.isBlank(str)) {
			throw new IllegalArgumentException("sox scan_packages must not be blank");
		}
		String[] a = str.split(",");
		List<String> l = new ArrayList<String>(a.length);
		for(String s : a) {
			if(StringUtils.isNotBlank(s)) {
				l.add(StringUtils.trim(s));
			}
		}
		
		String[] packages = l.toArray(new String[l.size()]);
		
		sc = new AnnontationConfigParser().scan(packages);
		
		sc.setDomain(this.getInitParameter(PARAMETER_DOMAIN, null));
		sc.setStorageType(StorageType.valueOf(this.getInitParameter(PARAMETER_STORAGE_TYPE, StorageType.COOKIE.name())));
		sc.setTimeout(timeout);
		sc.setEncrypterAlgorithm(this.getInitParameter(PARAMETER_ENCRYPTER_ALGORITHM, EncrypterAlgorithm.BLOWFISH.name()));
		sc.setPrivateKey(this.getInitParameter(PARAMETER_PRIVATE_KEY, SoxConstants.DEFAULT_KEY));
		
		SessionAttributeConfiguration sac = new SessionAttributeConfiguration();
		sac.setSystemAttribute(true);
		sac.setKey(SystemAttribute.SOX_SID.name());
		sac.setCompress(false);
		sac.setCookiePath(SoxConstants.DEFAULT_COOKIE_PATH);
		// 让ajax请求能带上来
		sac.setHttpOnly(true);
		
		sc.put(SystemAttribute.SOX_LAST_VISIT_TIME.name(), sac);
		
		sac = new SessionAttributeConfiguration();
		sac.setSystemAttribute(true);
		sac.setKey(SystemAttribute.SOX_LAST_VISIT_TIME.name());
		sac.setCompress(false);
		sac.setCookiePath(SoxConstants.DEFAULT_COOKIE_PATH);
		// 让ajax请求能带上来
		sac.setHttpOnly(true);
		
		sc.put(SystemAttribute.SOX_SID.name(), sac);
		
		SingletonSessionManagerFactory.initialize(sc);
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// 
		SoxHttpContext httpContext = new SoxHttpContext(request, response, this.getServletContext(), EncrypterFactory.newEncrypter(EncrypterAlgorithm.valueOf(sc.getEncrypterAlgorithm()), sc.getPrivateKey()));
		SoxRequest req = new SoxRequest(httpContext);
		SoxResponse res = new SoxResponse(response, (SoxSession)req.getSession());

        //req.setAttribute("LAZY_COMMIT_RESPONSE", Boolean.TRUE);
        
        try {
        	chain.doFilter(req, res);
        } finally {
        	res.commit();
        }
	}

	@Override
	protected void releaseResource() {
		
	}
}
