package molecule.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Java functions available in Datomic queries */
public class JavaFunctions
{
    public static long bind( long e )
    {
        return e;
    }

    public static Date date( String s ) throws ParseException
    {
        return ( new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" ).parse( s ) );
    }
}
