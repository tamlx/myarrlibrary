package b.laixuantam.myaarlibrary.api;

import java.util.HashMap;


public class ApiManagement
{
    HashMap<String, ApiRequest> requests = new HashMap<>();

    public void call(Class<?> request)
    {
        call(request, null, null);
    }

    public void call(Class<?> request, BaseApiParams params)
    {
        call(request, params, null);
    }

    public void call(Class<?> request, BaseApiParams params, ApiRequest.ApiCallback<?> callback)
    {
        if (request.getSuperclass() == ApiRequest.class)
        {
            ApiRequest.ApiName annotation = request.getAnnotation(ApiRequest.ApiName.class);
            String apiName = annotation.value();
            try
            {
                ApiRequest r;
                if (!requests.containsKey(apiName))
                {
                    r = (ApiRequest) request.newInstance();
                    requests.put(apiName, r);
                }
                else
                {
                    r = requests.get(apiName);
                }
                if (r != null)
                {
                    if (params == null)
                    {
                        params = new BaseApiParams();
                    }

                    r.setParams(params);
                    r.process(r.call(params), callback);
                }
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }
}