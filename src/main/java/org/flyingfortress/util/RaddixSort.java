package org.flyingfortress.util;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: vishnuhr
 * Date: 16/12/13
 * Time: 2:46 PM
 */
public class RaddixSort {
    /*
     * Radix sort an array of Strings
     * Assume all are all english alphabet
     * Assume all have length bounded by maxlen.
     * max number of buckets taken 256. todo: review this algo.
     */
    public static void radixSort( String [ ] arr, int maxLen )
    {
        final int bucket_size = 256;

        ArrayList<String>[ ] wordsByLength = new ArrayList[ maxLen + 1 ];
        ArrayList<String> [ ] buckets = new ArrayList[ bucket_size ];

        for( int i = 0; i < wordsByLength.length; i++ )
            wordsByLength[ i ] = new ArrayList( );

        for( int i = 0; i < bucket_size; i++ )
            buckets[ i ] = new ArrayList( );

        for( String s : arr )
            wordsByLength[ s.length( ) ].add( s );

        int idx = 0;
        for( ArrayList<String> wordList : wordsByLength )
            for( String s : wordList )
                arr[ idx++ ] = s;

        int startingIndex = arr.length;
        for( int pos = maxLen - 1; pos >= 0; pos-- )
        {
            startingIndex -= wordsByLength[ pos + 1 ].size( );

            for( int i = startingIndex; i < arr.length; i++ )
                buckets[ arr[ i ].charAt( pos ) ].add( arr[ i ] );

            idx = startingIndex;
            for( ArrayList<String> thisBucket : buckets )
            {
                for( String s : thisBucket )
                    arr[ idx++ ] = s;

                thisBucket.clear( );
            }
        }
    }

}
