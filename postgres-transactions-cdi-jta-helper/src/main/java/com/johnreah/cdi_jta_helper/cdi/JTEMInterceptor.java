package com.johnreah.cdi_jta_helper.cdi;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.Serializable;

/**
 * join transaction entity manager interceptor
 * @author Michael Krauter
 *
 */

@Interceptor
@Transactional
public class JTEMInterceptor implements Serializable {

	/**
		 * 
		 */
	private static final long serialVersionUID = -4795149781185912413L;

	@Inject
	private EntityManager em;

	@AroundInvoke
	public Object joinTransaction(InvocationContext ctx) throws Exception {
		if (!em.isJoinedToTransaction()) {
			em.joinTransaction();
		}
		return ctx.proceed();
	}
}
