/*
 * opentech inc.
 * sox
 */
package com.openteach.sox;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.openteach.sox.session.SoxSession;

/**
 * 对HttpServletResponse的包装, 确保cookie正常
 * @author sihai
 *
 */
public class SoxResponse extends HttpServletResponseWrapper {

	private static final Log logger = LogFactory.getLog(SoxResponse.class);
	
	private boolean flushed;					//
	
	private SoxSession session;					//
	
	private ServletOutputStream sos;			//
	
	private PrintWriter pw;						//
	 
	/**
	 * 构造函数
	 * @param response
	 * @param session
	 */
    public SoxResponse(HttpServletResponse response, SoxSession session) {
        super(response);
        this.session = session;
        this.flushed = false;
        
        setHeader("P3P", "CP='CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR'");
    }
    
    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
    }
    
    @Override
    public void sendRedirect(String location) throws IOException {
    	session.commit();
    	super.sendRedirect(location);
    }
    
    @Override
    public void sendError(int status) throws IOException {
    	session.commit();
        sendError(status, null);
    }
    
    @Override
    public void sendError(int status, String message) throws IOException {
        super.setStatus(status);
    }
    
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
    	if(null == this.sos) {
    		this.sos = new ServletOutputStreamWrapper(super.getOutputStream());
    	}
		return this.sos;
    }
    
    @Override
    public PrintWriter getWriter() throws IOException {
    	if(null == this.pw) {
    		this.pw = new PrintWriterWrapper(super.getWriter());
    	}
		return this.pw;
	}

    /**
     * 设置content长度。无效。
     *
     * @param length content长度
     */
    @Override
    public void setContentLength(int length) {
    	super.setContentLength(length);
    }
    
    /**
     * 对writer、stream都进行判空，如果都为null，则执行父类的方法
     */
    @Override
    public void flushBuffer() throws IOException {
    	session.commit();
    	super.flushBuffer();
    	this.flushed = true;
    }
    
    @Override
    public void resetBuffer() {
        super.resetBuffer();
    }
    
    /**
     * 提交response
     * @throws IOException
     */
    public void commit() throws IOException {
    	if(!this.flushed) {
    		this.flushBuffer();
    	}
    }
    
    /**
     * 
     */
    private class ServletOutputStreamWrapper extends ServletOutputStream {
    	
    	private ServletOutputStream delegate;
    	
    	public void write(int b) throws IOException {
			delegate.write(b);
		}

		public int hashCode() {
			return delegate.hashCode();
		}

		public void write(byte[] b) throws IOException {
			delegate.write(b);
		}

		public void write(byte[] b, int off, int len) throws IOException {
			delegate.write(b, off, len);
		}

		public boolean equals(Object obj) {
			return delegate.equals(obj);
		}

		public void print(String s) throws IOException {
			delegate.print(s);
		}

		public void flush() throws IOException {
			SoxResponse.this.session.commit();
			delegate.flush();
		}

		public void print(boolean b) throws IOException {
			delegate.print(b);
		}

		public void close() throws IOException {
			delegate.close();
		}

		public void print(char c) throws IOException {
			delegate.print(c);
		}

		public void print(int i) throws IOException {
			delegate.print(i);
		}

		public void print(long l) throws IOException {
			delegate.print(l);
		}

		public void print(float f) throws IOException {
			delegate.print(f);
		}

		public void print(double d) throws IOException {
			delegate.print(d);
		}

		public void println() throws IOException {
			delegate.println();
		}

		public void println(String s) throws IOException {
			delegate.println(s);
		}

		public void println(boolean b) throws IOException {
			delegate.println(b);
		}

		public void println(char c) throws IOException {
			delegate.println(c);
		}

		public String toString() {
			return delegate.toString();
		}

		public void println(int i) throws IOException {
			delegate.println(i);
		}

		public void println(long l) throws IOException {
			delegate.println(l);
		}

		public void println(float f) throws IOException {
			delegate.println(f);
		}

		public void println(double d) throws IOException {
			delegate.println(d);
		}

		/**
    	 * 
    	 * @param delegate
    	 */
    	public ServletOutputStreamWrapper(ServletOutputStream delegate) {
    		this.delegate = delegate;
    	}
    }
    
    /**
     * 
     */
    private class PrintWriterWrapper extends PrintWriter {
    	
    	private PrintWriter delegate;
    	
    	public int hashCode() {
			return delegate.hashCode();
		}

		public boolean equals(Object obj) {
			return delegate.equals(obj);
		}

		public String toString() {
			return delegate.toString();
		}

		public void flush() {
			SoxResponse.this.session.commit();
			delegate.flush();
		}

		public void close() {
			delegate.close();
		}

		public boolean checkError() {
			return delegate.checkError();
		}

		public void write(int c) {
			delegate.write(c);
		}

		public void write(char[] buf, int off, int len) {
			delegate.write(buf, off, len);
		}

		public void write(char[] buf) {
			delegate.write(buf);
		}

		public void write(String s, int off, int len) {
			delegate.write(s, off, len);
		}

		public void write(String s) {
			delegate.write(s);
		}

		public void print(boolean b) {
			delegate.print(b);
		}

		public void print(char c) {
			delegate.print(c);
		}

		public void print(int i) {
			delegate.print(i);
		}

		public void print(long l) {
			delegate.print(l);
		}

		public void print(float f) {
			delegate.print(f);
		}

		public void print(double d) {
			delegate.print(d);
		}

		public void print(char[] s) {
			delegate.print(s);
		}

		public void print(String s) {
			delegate.print(s);
		}

		public void print(Object obj) {
			delegate.print(obj);
		}

		public void println() {
			delegate.println();
		}

		public void println(boolean x) {
			delegate.println(x);
		}

		public void println(char x) {
			delegate.println(x);
		}

		public void println(int x) {
			delegate.println(x);
		}

		public void println(long x) {
			delegate.println(x);
		}

		public void println(float x) {
			delegate.println(x);
		}

		public void println(double x) {
			delegate.println(x);
		}

		public void println(char[] x) {
			delegate.println(x);
		}

		public void println(String x) {
			delegate.println(x);
		}

		public void println(Object x) {
			delegate.println(x);
		}

		public PrintWriter printf(String format, Object... args) {
			return delegate.printf(format, args);
		}

		public PrintWriter printf(Locale l, String format, Object... args) {
			return delegate.printf(l, format, args);
		}

		public PrintWriter format(String format, Object... args) {
			return delegate.format(format, args);
		}

		public PrintWriter format(Locale l, String format, Object... args) {
			return delegate.format(l, format, args);
		}

		public PrintWriter append(CharSequence csq) {
			return delegate.append(csq);
		}

		public PrintWriter append(CharSequence csq, int start, int end) {
			return delegate.append(csq, start, end);
		}

		public PrintWriter append(char c) {
			return delegate.append(c);
		}

		public PrintWriterWrapper(PrintWriter pw) {
			super(pw);
    		this.delegate = pw;
    	}
    }
}
