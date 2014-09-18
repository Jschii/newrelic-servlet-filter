package yleisradio.newrelic

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import com.newrelic.api.agent.{Response, HeaderType, Request}


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