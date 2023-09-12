package com.google.android.gms.plus.model.moments;

import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.internal.fq;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public interface ItemScope extends Freezable<ItemScope> {

    /* loaded from: classes.dex */
    public static class Builder {
        private String hN;
        private String mName;
        private String mo;
        private double oE;
        private double oF;
        private final Set<Integer> rI = new HashSet();
        private fq rJ;
        private List<String> rK;
        private fq rL;
        private String rM;
        private String rN;
        private String rO;
        private List<fq> rP;
        private int rQ;
        private List<fq> rR;
        private fq rS;
        private List<fq> rT;
        private String rU;
        private String rV;
        private fq rW;
        private String rX;
        private String rY;
        private String rZ;
        private fq sA;
        private String sB;
        private String sC;
        private String sD;
        private String sE;
        private String sF;
        private List<fq> sa;
        private String sb;
        private String sc;
        private String sd;
        private String se;
        private String sf;
        private String sg;
        private String sh;
        private String si;
        private fq sj;
        private String sk;
        private String sl;
        private String sm;
        private String sn;
        private fq so;
        private fq sp;
        private fq sq;
        private List<fq> sr;
        private String ss;
        private String st;
        private String su;
        private String sv;
        private fq sw;
        private String sx;
        private String sy;
        private String sz;

        public ItemScope build() {
            return new fq(this.rI, this.rJ, this.rK, this.rL, this.rM, this.rN, this.rO, this.rP, this.rQ, this.rR, this.rS, this.rT, this.rU, this.rV, this.rW, this.rX, this.rY, this.rZ, this.sa, this.sb, this.sc, this.sd, this.mo, this.se, this.sf, this.sg, this.sh, this.si, this.sj, this.sk, this.sl, this.sm, this.sn, this.so, this.oE, this.sp, this.oF, this.mName, this.sq, this.sr, this.ss, this.st, this.su, this.sv, this.sw, this.sx, this.sy, this.sz, this.sA, this.sB, this.sC, this.sD, this.hN, this.sE, this.sF);
        }

        public Builder setAbout(ItemScope about) {
            this.rJ = (fq) about;
            this.rI.add(2);
            return this;
        }

        public Builder setAdditionalName(List<String> additionalName) {
            this.rK = additionalName;
            this.rI.add(3);
            return this;
        }

        public Builder setAddress(ItemScope address) {
            this.rL = (fq) address;
            this.rI.add(4);
            return this;
        }

        public Builder setAddressCountry(String addressCountry) {
            this.rM = addressCountry;
            this.rI.add(5);
            return this;
        }

        public Builder setAddressLocality(String addressLocality) {
            this.rN = addressLocality;
            this.rI.add(6);
            return this;
        }

        public Builder setAddressRegion(String addressRegion) {
            this.rO = addressRegion;
            this.rI.add(7);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder setAssociated_media(List<ItemScope> associated_media) {
            this.rP = associated_media;
            this.rI.add(8);
            return this;
        }

        public Builder setAttendeeCount(int attendeeCount) {
            this.rQ = attendeeCount;
            this.rI.add(9);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder setAttendees(List<ItemScope> attendees) {
            this.rR = attendees;
            this.rI.add(10);
            return this;
        }

        public Builder setAudio(ItemScope audio) {
            this.rS = (fq) audio;
            this.rI.add(11);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder setAuthor(List<ItemScope> author) {
            this.rT = author;
            this.rI.add(12);
            return this;
        }

        public Builder setBestRating(String bestRating) {
            this.rU = bestRating;
            this.rI.add(13);
            return this;
        }

        public Builder setBirthDate(String birthDate) {
            this.rV = birthDate;
            this.rI.add(14);
            return this;
        }

        public Builder setByArtist(ItemScope byArtist) {
            this.rW = (fq) byArtist;
            this.rI.add(15);
            return this;
        }

        public Builder setCaption(String caption) {
            this.rX = caption;
            this.rI.add(16);
            return this;
        }

        public Builder setContentSize(String contentSize) {
            this.rY = contentSize;
            this.rI.add(17);
            return this;
        }

        public Builder setContentUrl(String contentUrl) {
            this.rZ = contentUrl;
            this.rI.add(18);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder setContributor(List<ItemScope> contributor) {
            this.sa = contributor;
            this.rI.add(19);
            return this;
        }

        public Builder setDateCreated(String dateCreated) {
            this.sb = dateCreated;
            this.rI.add(20);
            return this;
        }

        public Builder setDateModified(String dateModified) {
            this.sc = dateModified;
            this.rI.add(21);
            return this;
        }

        public Builder setDatePublished(String datePublished) {
            this.sd = datePublished;
            this.rI.add(22);
            return this;
        }

        public Builder setDescription(String description) {
            this.mo = description;
            this.rI.add(23);
            return this;
        }

        public Builder setDuration(String duration) {
            this.se = duration;
            this.rI.add(24);
            return this;
        }

        public Builder setEmbedUrl(String embedUrl) {
            this.sf = embedUrl;
            this.rI.add(25);
            return this;
        }

        public Builder setEndDate(String endDate) {
            this.sg = endDate;
            this.rI.add(26);
            return this;
        }

        public Builder setFamilyName(String familyName) {
            this.sh = familyName;
            this.rI.add(27);
            return this;
        }

        public Builder setGender(String gender) {
            this.si = gender;
            this.rI.add(28);
            return this;
        }

        public Builder setGeo(ItemScope geo) {
            this.sj = (fq) geo;
            this.rI.add(29);
            return this;
        }

        public Builder setGivenName(String givenName) {
            this.sk = givenName;
            this.rI.add(30);
            return this;
        }

        public Builder setHeight(String height) {
            this.sl = height;
            this.rI.add(31);
            return this;
        }

        public Builder setId(String id) {
            this.sm = id;
            this.rI.add(32);
            return this;
        }

        public Builder setImage(String image) {
            this.sn = image;
            this.rI.add(33);
            return this;
        }

        public Builder setInAlbum(ItemScope inAlbum) {
            this.so = (fq) inAlbum;
            this.rI.add(34);
            return this;
        }

        public Builder setLatitude(double latitude) {
            this.oE = latitude;
            this.rI.add(36);
            return this;
        }

        public Builder setLocation(ItemScope location) {
            this.sp = (fq) location;
            this.rI.add(37);
            return this;
        }

        public Builder setLongitude(double longitude) {
            this.oF = longitude;
            this.rI.add(38);
            return this;
        }

        public Builder setName(String name) {
            this.mName = name;
            this.rI.add(39);
            return this;
        }

        public Builder setPartOfTVSeries(ItemScope partOfTVSeries) {
            this.sq = (fq) partOfTVSeries;
            this.rI.add(40);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Builder setPerformers(List<ItemScope> performers) {
            this.sr = performers;
            this.rI.add(41);
            return this;
        }

        public Builder setPlayerType(String playerType) {
            this.ss = playerType;
            this.rI.add(42);
            return this;
        }

        public Builder setPostOfficeBoxNumber(String postOfficeBoxNumber) {
            this.st = postOfficeBoxNumber;
            this.rI.add(43);
            return this;
        }

        public Builder setPostalCode(String postalCode) {
            this.su = postalCode;
            this.rI.add(44);
            return this;
        }

        public Builder setRatingValue(String ratingValue) {
            this.sv = ratingValue;
            this.rI.add(45);
            return this;
        }

        public Builder setReviewRating(ItemScope reviewRating) {
            this.sw = (fq) reviewRating;
            this.rI.add(46);
            return this;
        }

        public Builder setStartDate(String startDate) {
            this.sx = startDate;
            this.rI.add(47);
            return this;
        }

        public Builder setStreetAddress(String streetAddress) {
            this.sy = streetAddress;
            this.rI.add(48);
            return this;
        }

        public Builder setText(String text) {
            this.sz = text;
            this.rI.add(49);
            return this;
        }

        public Builder setThumbnail(ItemScope thumbnail) {
            this.sA = (fq) thumbnail;
            this.rI.add(50);
            return this;
        }

        public Builder setThumbnailUrl(String thumbnailUrl) {
            this.sB = thumbnailUrl;
            this.rI.add(51);
            return this;
        }

        public Builder setTickerSymbol(String tickerSymbol) {
            this.sC = tickerSymbol;
            this.rI.add(52);
            return this;
        }

        public Builder setType(String type) {
            this.sD = type;
            this.rI.add(53);
            return this;
        }

        public Builder setUrl(String url) {
            this.hN = url;
            this.rI.add(54);
            return this;
        }

        public Builder setWidth(String width) {
            this.sE = width;
            this.rI.add(55);
            return this;
        }

        public Builder setWorstRating(String worstRating) {
            this.sF = worstRating;
            this.rI.add(56);
            return this;
        }
    }

    ItemScope getAbout();

    List<String> getAdditionalName();

    ItemScope getAddress();

    String getAddressCountry();

    String getAddressLocality();

    String getAddressRegion();

    List<ItemScope> getAssociated_media();

    int getAttendeeCount();

    List<ItemScope> getAttendees();

    ItemScope getAudio();

    List<ItemScope> getAuthor();

    String getBestRating();

    String getBirthDate();

    ItemScope getByArtist();

    String getCaption();

    String getContentSize();

    String getContentUrl();

    List<ItemScope> getContributor();

    String getDateCreated();

    String getDateModified();

    String getDatePublished();

    String getDescription();

    String getDuration();

    String getEmbedUrl();

    String getEndDate();

    String getFamilyName();

    String getGender();

    ItemScope getGeo();

    String getGivenName();

    String getHeight();

    String getId();

    String getImage();

    ItemScope getInAlbum();

    double getLatitude();

    ItemScope getLocation();

    double getLongitude();

    String getName();

    ItemScope getPartOfTVSeries();

    List<ItemScope> getPerformers();

    String getPlayerType();

    String getPostOfficeBoxNumber();

    String getPostalCode();

    String getRatingValue();

    ItemScope getReviewRating();

    String getStartDate();

    String getStreetAddress();

    String getText();

    ItemScope getThumbnail();

    String getThumbnailUrl();

    String getTickerSymbol();

    String getType();

    String getUrl();

    String getWidth();

    String getWorstRating();

    boolean hasAbout();

    boolean hasAdditionalName();

    boolean hasAddress();

    boolean hasAddressCountry();

    boolean hasAddressLocality();

    boolean hasAddressRegion();

    boolean hasAssociated_media();

    boolean hasAttendeeCount();

    boolean hasAttendees();

    boolean hasAudio();

    boolean hasAuthor();

    boolean hasBestRating();

    boolean hasBirthDate();

    boolean hasByArtist();

    boolean hasCaption();

    boolean hasContentSize();

    boolean hasContentUrl();

    boolean hasContributor();

    boolean hasDateCreated();

    boolean hasDateModified();

    boolean hasDatePublished();

    boolean hasDescription();

    boolean hasDuration();

    boolean hasEmbedUrl();

    boolean hasEndDate();

    boolean hasFamilyName();

    boolean hasGender();

    boolean hasGeo();

    boolean hasGivenName();

    boolean hasHeight();

    boolean hasId();

    boolean hasImage();

    boolean hasInAlbum();

    boolean hasLatitude();

    boolean hasLocation();

    boolean hasLongitude();

    boolean hasName();

    boolean hasPartOfTVSeries();

    boolean hasPerformers();

    boolean hasPlayerType();

    boolean hasPostOfficeBoxNumber();

    boolean hasPostalCode();

    boolean hasRatingValue();

    boolean hasReviewRating();

    boolean hasStartDate();

    boolean hasStreetAddress();

    boolean hasText();

    boolean hasThumbnail();

    boolean hasThumbnailUrl();

    boolean hasTickerSymbol();

    boolean hasType();

    boolean hasUrl();

    boolean hasWidth();

    boolean hasWorstRating();
}
