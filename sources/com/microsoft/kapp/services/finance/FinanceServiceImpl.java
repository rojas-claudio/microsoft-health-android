package com.microsoft.kapp.services.finance;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.microsoft.kapp.services.ServiceException;
import com.microsoft.kapp.style.utils.MetricSpannerUtils;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GsonUtils;
import com.microsoft.krestsdk.services.KRestException;
import com.microsoft.krestsdk.services.NetworkProvider;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class FinanceServiceImpl implements FinanceService {
    private NetworkProvider mNetworkProvider;

    public FinanceServiceImpl(NetworkProvider provider) {
        this.mNetworkProvider = provider;
    }

    @Override // com.microsoft.kapp.services.finance.FinanceService
    public List<Stock> getStockInformation(Collection<String> symbols) throws ServiceException {
        try {
            if (symbols == null) {
                throw new IllegalArgumentException("symbols cannot be null.");
            }
            StringBuilder symbolList = new StringBuilder();
            Iterator<String> iterator = symbols.iterator();
            while (iterator.hasNext()) {
                symbolList.append(iterator.next());
                if (iterator.hasNext()) {
                    symbolList.append("%2C");
                }
            }
            String financeUrl = String.format(Constants.FINANCE_URL_FORMAT, symbolList.toString());
            String providerResponse = this.mNetworkProvider.executeHttpGet(financeUrl, null);
            JSONArray stockList = new JSONArray(providerResponse);
            List<Stock> result = new ArrayList<>();
            for (int i = 0; i < stockList.length(); i++) {
                JSONObject stockObject = stockList.getJSONObject(i);
                String companyName = "";
                if (stockObject.has(Constants.STOCK_COMPANY_NAME_KEY)) {
                    companyName = stockObject.getString(Constants.STOCK_COMPANY_NAME_KEY);
                }
                if (companyName.contains("!") || companyName.contains("$")) {
                    companyName = stockObject.getString(Constants.STOCK_COMPANY_NAME_ALTERNATE_KEY);
                }
                double price = Constants.SPLITS_ACCURACY;
                if (stockObject.has(Constants.STOCK_COMPANY_PRICE_KEY)) {
                    price = stockObject.getDouble(Constants.STOCK_COMPANY_PRICE_KEY);
                }
                double change = Constants.SPLITS_ACCURACY;
                if (stockObject.has(Constants.STOCK_COMPANY_CHANGE_KEY)) {
                    change = stockObject.getDouble(Constants.STOCK_COMPANY_CHANGE_KEY);
                }
                int priceCents = (int) Math.round(100.0d * price);
                int changeCents = (int) Math.round(100.0d * change);
                String stockName = MetricSpannerUtils.METRIC_UNITS_SEPARATOR_SPACE;
                if (stockObject.has(Constants.STOCK_COMPANY_SYMBOL_KEY)) {
                    stockName = stockObject.getString(Constants.STOCK_COMPANY_SYMBOL_KEY);
                }
                Stock resultStock = new Stock(stockName, priceCents, changeCents, companyName);
                result.add(resultStock);
            }
            return result;
        } catch (IOException exception) {
            throw new ServiceException("IO Error calling service.", exception);
        } catch (URISyntaxException exception2) {
            throw new ServiceException("URL syntax error calling service.", exception2);
        } catch (JSONException exception3) {
            throw new ServiceException("Error in JSON from service", exception3);
        }
    }

    @Override // com.microsoft.kapp.services.finance.FinanceService
    public ArrayList<StockCompanyInformation> getStockCompanies(String stockCompanyName) throws KRestException {
        try {
            String response = this.mNetworkProvider.executeHttpGet(String.format(Constants.FINANCE_COMPANY_LOOKUP_URL_FORMAT, stockCompanyName), null);
            JsonObject baseObject = new JsonParser().parse(response).getAsJsonObject();
            if (baseObject.get("data").isJsonArray()) {
                JsonArray responses = baseObject.getAsJsonArray("data");
                return (ArrayList) GsonUtils.getCustomDeserializer().fromJson(responses, new TypeToken<List<StockCompanyInformation>>() { // from class: com.microsoft.kapp.services.finance.FinanceServiceImpl.1
                }.getType());
            }
            return null;
        } catch (JsonParseException exception) {
            throw new KRestException("Invalid JSON on stock response.", exception);
        } catch (IOException exception2) {
            throw new KRestException("Invalid JSON on stock response.", exception2);
        } catch (URISyntaxException exception3) {
            throw new KRestException("Invalid JSON on stock response.", exception3);
        }
    }
}
