package b.laixuantam.myaarlibrary.helper.map.direction;

import b.laixuantam.myaarlibrary.helper.map.direction.request.DirectionOriginRequest;

public class GoogleDirection
{
    public static DirectionOriginRequest withServerKey(String apiKey)
    {
        return new DirectionOriginRequest(apiKey);
    }
}
