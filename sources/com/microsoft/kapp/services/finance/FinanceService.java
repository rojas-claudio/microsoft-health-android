package com.microsoft.kapp.services.finance;

import com.microsoft.kapp.services.ServiceException;
import com.microsoft.krestsdk.services.KRestException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* loaded from: classes.dex */
public interface FinanceService {
    ArrayList<StockCompanyInformation> getStockCompanies(String str) throws KRestException;

    List<Stock> getStockInformation(Collection<String> collection) throws ServiceException;
}
