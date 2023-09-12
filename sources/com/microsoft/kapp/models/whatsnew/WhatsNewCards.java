package com.microsoft.kapp.models.whatsnew;

import android.content.res.Resources;
import com.microsoft.kapp.R;
import com.microsoft.kapp.models.whatsnew.WhatsNewCardDataModel;
import com.microsoft.kapp.services.SettingsProvider;
import com.microsoft.kapp.utils.Constants;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class WhatsNewCards {
    private static int CARDS_VERSION = 2;

    public static List<WhatsNewCardDataModel> getCards(Resources res) {
        List<WhatsNewCardDataModel> cards = new ArrayList<>();
        WhatsNewCardDataModel card1 = new WhatsNewCardDataModel();
        card1.setCardType(Constants.WHATSNEW_CARDTYPE_BAND2);
        card1.setTitle(res.getString(R.string.whatsnew_card_title_1));
        card1.setSubtitle(res.getString(R.string.whatsnew_card_subtitle_1));
        card1.setImageID(R.drawable.card1);
        card1.setButtonType(WhatsNewCardDataModel.ButtonType.WEB);
        card1.setButtonText(res.getString(R.string.whatsnew_card_button_text_1));
        card1.setUrl(Constants.BAND_HOME_URL);
        cards.add(card1);
        WhatsNewCardDataModel card2 = new WhatsNewCardDataModel();
        card2.setCardType(Constants.WHATSNEW_CARDTYPE_UV);
        card2.setTitle(res.getString(R.string.whatsnew_card_title_2));
        card2.setSubtitle(res.getString(R.string.whatsnew_card_subtitle_2));
        card2.setImageID(R.drawable.card2);
        card2.setButtonType(WhatsNewCardDataModel.ButtonType.WEB);
        card2.setButtonText(res.getString(R.string.whatsnew_card_button_text_2));
        card2.setUrl(Constants.BAND_HOME_URL);
        cards.add(card2);
        WhatsNewCardDataModel card3 = new WhatsNewCardDataModel();
        card3.setCardType(Constants.WHATSNEW_CARDTYPE_STAIRS);
        card3.setTitle(res.getString(R.string.whatsnew_card_title_3));
        card3.setSubtitle(res.getString(R.string.whatsnew_card_subtitle_3));
        card3.setImageID(R.drawable.card3);
        card3.setButtonType(WhatsNewCardDataModel.ButtonType.WEB);
        card3.setButtonText(res.getString(R.string.whatsnew_card_button_text_3));
        card3.setUrl(Constants.BAND_HOME_URL);
        cards.add(card3);
        WhatsNewCardDataModel card4 = new WhatsNewCardDataModel();
        card4.setCardType(Constants.WHATSNEW_CARDTYPE_SMARTALARM);
        card4.setTitle(res.getString(R.string.whatsnew_card_title_4));
        card4.setSubtitle(res.getString(R.string.whatsnew_card_subtitle_4));
        card4.setImageID(R.drawable.card4);
        card4.setButtonType(WhatsNewCardDataModel.ButtonType.WEB);
        card4.setButtonText(res.getString(R.string.whatsnew_card_button_text_4));
        card4.setUrl(Constants.BAND_HOME_URL);
        cards.add(card4);
        WhatsNewCardDataModel card5 = new WhatsNewCardDataModel();
        card5.setCardType(Constants.WHATSNEW_CARDTYPE_CUSTOMWORKOUT);
        card5.setTitle(res.getString(R.string.whatsnew_card_title_5));
        card5.setSubtitle(res.getString(R.string.whatsnew_card_subtitle_5));
        card5.setImageID(R.drawable.card5);
        card5.setButtonType(WhatsNewCardDataModel.ButtonType.NONE);
        cards.add(card5);
        return cards;
    }

    public static boolean isCardsVersionChanged(SettingsProvider mSettingsProvider) {
        int cardVersionInSetting = mSettingsProvider.getWhatsNewCardsVersion();
        return cardVersionInSetting != CARDS_VERSION;
    }

    public static void updateCardVersionInSetting(SettingsProvider mSettingsProvider) {
        mSettingsProvider.setWhatsNewCardsVersion(CARDS_VERSION);
    }
}
