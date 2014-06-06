package yle.newrelic

import com.newrelic.api.agent.{NewRelic,Trace,Request,Response,HeaderType}
import javax.servlet._
import javax.servlet.http._

class NewRelicTracingFilter extends Filter {
  def init(cfg: FilterConfig) {

  }

  def destroy() {

  }

  def doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
    traceRequest(request.asInstanceOf[HttpServletRequest], response.asInstanceOf[HttpServletResponse]) {
      filterChain.doFilter(request, response)
    }
  }

  @Trace(dispatcher=true)
  def traceRequest(request: HttpServletRequest, response: HttpServletResponse)(block: => Any) = {
    val nrRequest = NewRelicRequest(request)
    val nrResponse = NewRelicResponse(response)

    NewRelic.setTransactionName("api", s"${nrRequest.getRequestURI} ${request.getMethod}")
    NewRelic.setRequestAndResponse(nrRequest, nrResponse)

    try {
      block
    } catch {
      case ex: Throwable => {
        NewRelic.noticeError(ex)
        throw ex
      }
    }
  }
}

case class NewRelicRequest(underlying: HttpServletRequest) extends Request {
  def getRequestURI = Option(underlying.getRequestURI).filter(_.nonEmpty).getOrElse("/")
  def getCookieValue(name: String) = underlying.getCookies.find(_.getName == name).map(_.getValue).getOrElse(null)
  def getHeader(name: String) = underlying.getHeader(name)
  def getRemoteUser = null
  def getParameterNames = underlying.getParameterNames
  def getParameterValues(name: String) = underlying.getParameterValues(name)
  def getAttribute(name: String) = underlying.getAttribute(name)
  def getHeaderType = HeaderType.HTTP
}

case class NewRelicResponse(underlying: HttpServletResponse) extends Response {
  def getStatus = underlying.getStatus
  def getStatusMessage = null
  def setHeader(name: String, value: String) = underlying.setHeader(name, value)
  def getHeaderType = HeaderType.HTTP
  def getContentType = underlying.getContentType
}

