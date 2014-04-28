package cn.cafebabe.autodao.exception;

public class AutodaoException extends RuntimeException
{
	private static final long serialVersionUID = 9120578618837080999L;
	
	public AutodaoException()
	{
		super();
	}
	
	public AutodaoException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public AutodaoException(String message)
	{
		super(message);
	}
	
	public AutodaoException(Throwable cause)
	{
		super(cause);
	}
}
