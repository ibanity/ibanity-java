package com.ibanity.apis.client.products.ponto_connect.services;

import com.ibanity.apis.client.products.ponto_connect.models.Userinfo;
import com.ibanity.apis.client.products.ponto_connect.models.read.UserinfoReadQuery;

public interface UserinfoService {

    Userinfo getUserinfo(UserinfoReadQuery readQuery);
}
