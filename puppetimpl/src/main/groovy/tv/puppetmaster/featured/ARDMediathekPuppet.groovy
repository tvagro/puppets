package tv.puppetmaster.featured

import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import tv.puppetmaster.data.i.*
import tv.puppetmaster.data.i.Puppet.PuppetIterator

import java.util.regex.Matcher

class ARDMediathekPuppet implements InstallablePuppet {

    static final int VERSION_CODE = 5

    static final CHANNELS = [
            [
                    name:    "Erstes Deutsches Fernsehen",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://daserste_live-lh.akamaihd.net/i/daserste_de@91204/master.m3u8",
                    ],
            ],
            [
                    name:    "ARD-alpha",
                    description:    "Livestream",
                    genres:         "TECHNOLOGY",
                    urls:           [
                            "http://livestreams.br.de/i/bralpha_germany@119899/master.m3u8",
                    ],
            ],
            [
                    name:    "ARTE deutsch",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://artelive-lh.akamaihd.net/i/artelive_de@393591/master.m3u8",
                    ],
            ],
            [
                    name:    "BR Fernsehen Süd",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://livestreams.br.de/i/bfssued_germany@119890/master.m3u8",
                    ],
            ],
            [
                    name:    "Das Erste",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://live-lh.daserste.de/i/daserste_de@91204/master.m3u8",
                    ],
            ],
            [
                    name:    "Deutsche Welle",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://dwstream72-lh.akamaihd.net/i/dwstream72_live@123556/master.m3u8",
                    ],
            ],
            [
                    name:    "hr-fernsehen",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://livestream-1.hr.de/i/hr_fernsehen@75910/master.m3u8",
                    ],
            ],
            [
                    name:    "KiKA",
                    description:    "Livestream",
                    genres:         "CHILDREN",
                    urls:           [
                            "http://kika_geo-lh.akamaihd.net/i/livetvkika_de@75114/master.m3u8",
                    ],
            ],
            [
                    name:    "MDR SACHSEN",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://livetvsachsen.mdr.de/i/livetvmdrsachsen_de@106902/master.m3u8",
                    ],
            ],
            [
                    name:    "NDR Niedersachsen",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://ndr_fs-lh.akamaihd.net/i/ndrfs_nds@119224/master.m3u8",
                    ],
            ],
            [
                    name:    "ONE",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://wdr_einsfestival-lh.akamaihd.net/i/wdr_einsfestival@328300/master.m3u8",
                    ],
            ],
            [
                    name:    "Phoenix",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://zdf0910-lh.akamaihd.net/i/de09_v1@392871/master.m3u8?dw=0",
                    ],
            ],
            [
                    name:    "rbb Fernsehen",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://tv-live.rbb-online.de/i/rbb_brandenburg@107638/master.m3u8",
                    ],
            ],
            [
                    name:    "SR Fernsehen",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://fs.live.sr.de/i/sr_universal02@107595/master.m3u8",
                    ],
            ],
            [
                    name:    "SWR Baden-Württemberg",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://swrbw-lh.akamaihd.net/i/swrbw_live@196738/master.m3u8",
                    ],
            ],
            [
                    name:    "tagesschau24",
                    description:    "Livestream",
                    genres:         "NEWS",
                    urls:           [
                            "http://tagesschau-lh.akamaihd.net/i/tagesschau_1@119231/master.m3u8",
                    ],
            ],
            [
                    name:    "WDR Fernsehen",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://tvstreamgeo.wdr.de/i/wdrfs_geogeblockt@112044/master.m3u8",
                    ],
            ],
            [
                    name:    "3sat",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://zdf0910-lh.akamaihd.net/i/dach10_v1@392872/master.m3u8?dw=0",
                    ],
            ],
    ]

    def ParentPuppet mParent
    def boolean mIsTopLevel
    def String mName
    def String mDescription
    def String mImageUrl
    def String mBackgroundImageUrl
    def String mUrl

    ARDMediathekPuppet() {
        this(
                null,
                true,
                "ARD Mediathek",
                "Das Erste, Einsfestival, EinsPlus, Phoenix, WDR, SWR, HR, MDR, NDR, RBB, BR, ... Mediathek",
                null,
                null,
                "http://www.ardmediathek.de/"
        )
    }

    ARDMediathekPuppet(ParentPuppet parent, boolean isTopLevel, String name, String description, String imageUrl, String backgroundImageUrl, String url) {
        mParent = parent
        mIsTopLevel = isTopLevel
        mName = name
        mDescription = description
        mImageUrl = imageUrl
        mBackgroundImageUrl = backgroundImageUrl
        mUrl = url
    }

    @Override
    int getVersionCode() {
        return VERSION_CODE
    }

    @Override
    SearchesPuppet getSearchProvider() {
        return new ARDMediathekSearchesPuppet(this)
    }

    @Override
    SettingsPuppet getSettingsProvider() {
        return null
    }

    @Override
    int getFastlaneBackgroundColor() {
        return 0xFF002651
    }

    @Override
    int getSearchAffordanceBackgroundColor() {
        return 0xFF002651
    }

    @Override
    int getSelectedBackgroundColor() {
        return 0xFF002651
    }

    @Override
    int getPlayerBackgroundColor() {
        return 0xFF002651
    }

    @Override
    List<Map<String, String>> getLiveChannelsMetaData() {
        def list = []
        CHANNELS.each { source ->
            list << [
                    name                : source.name,
                    description         : source.description,
                    genres              : source.genres,
                    preferredRegion     : 'de',
                    logo                : getImageUrl(),
                    url                 : source.urls[0]
            ]
        }
        return list
    }

    @Override
    PuppetIterator getChildren() {
        PuppetIterator children = new ARDMediathekPuppetIterator()

        def Document document = Jsoup.connect(mUrl).get()

        document.select(".teaser").each {
            def boolean isCategory = false
            try {
                def JSONObject trigger = null
                if (it.hasAttr("data-ctrl-23644100-trigger")) {
                    trigger = new JSONObject(it.attr("data-ctrl-23644100-trigger"))
                } else if (it.hasAttr("data-ctrl-21282666-21282550-trigger")) {
                    trigger = new JSONObject(it.attr("data-ctrl-21282666-21282550-trigger"))
                }
                if (trigger) {
                    def JSONObject teaserTracking = trigger.getJSONObject("teasertracking")
                    isCategory = (teaserTracking.has("D") && teaserTracking.getString("D") == "Themenpaket") ||
                            teaserTracking.getString("F") == "Rubriken%20%28ModMiniPanel%29" ||
                            teaserTracking.getString("F") == "Serien%20%20%28ModMiniPanel%29"
                }
            } catch (ignore) {}
            def String title = it.select(".headline") ? it.select(".headline").first().text() : null
            if (!title && it.select("img")) {
                title = it.select("img").first().attr("alt")
            }
            def String description = it.select(".subtitle") ? it.select(".subtitle").first().text() : null
            def String publicationDate = null
            if (description != null) {
                publicationDate = description.split("<br>")[0]
                description = description.split("<br>")[-1]
            }
            if (it.select(".dachzeile")) {
                description = it.select(".dachzeile").first().text() + (description ? " - " + description : "")
            }
            def Element a = it.select("a").first()
            def Element img = it.select("img") ? it.select("img").first() : null
            def String imageUrl = null
            if (img && img.hasAttr("data-ctrl-image")) {
                imageUrl = "http://www.ardmediathek.de" + new JSONObject(img.attr("data-ctrl-image")).getString("urlScheme")
            }
            if (a && isCategory) {
                children.add(new ARDMediathekPuppet(
                        this,
                        false,
                        title,
                        description,
                        imageUrl ? imageUrl.replace("##width##", "256") : null,
                        imageUrl ? imageUrl.replace("##width##", "1280") : null,
                        a.absUrl("href")
                ))
            } else if (a && !isCategory && !a.attr("href").startsWith("http://www.microsoft.com") &&
                    !a.attr("href").startsWith("/radio/") &&
                    !a.attr("href").startsWith("/ard/")) {
                children.add(new ARDMediathekSourcesPuppet(
                        this,
                        mName == "TV-Livestreams",
                        title,
                        description,
                        publicationDate,
                        imageUrl ? imageUrl.replace("##width##", "256") : null,
                        imageUrl ? imageUrl.replace("##width##", "1280") : null,
                        a.absUrl("href")
                ))
            }
        }
        document.select(".entry:not(.inactive):not(.active)").each {
            children.add(new ARDMediathekPuppet(
                    this,
                    false,
                    it.text(),
                    null,
                    null,
                    null,
                    it.select("a").first().absUrl("href")
            ))
        }

        if (mParent == null) {
            children.add(new ARDMediathekPuppet(
                    this,
                    true,
                    "TV-Livestreams",
                    "Die TV-Livestreams (Fernsehsignale live) der ARD (ARD-Alpha, Arte, BR, Das Erste, DW, Einsfestival, HR, KiKA, MDR, NDR, Phoenix, RBB, SR, SWR, Tagesschau24, WDR, 3sat) in der ARD Mediathek",
                    null,
                    null,
                    "http://www.ardmediathek.de/tv/live?kanal=Alle"
            ))
            children.add(new ARDMediathekPuppet(
                    this,
                    true,
                    "Rubriken",
                    null,
                    null,
                    null,
                    "http://www.ardmediathek.de/tv/Rubriken/mehr?documentId=21282550"
            ))
        }

        /*if (mParent == null) {
            document.select(".subressorts.raster a").each {
                children.add(new ARDMediathekPuppet(
                        this,
                        false,
                        it.text(),
                        null,
                        null,
                        null,
                        it.absUrl("href")
                ))
            }
        }*/

        return children
    }

    @Override
    boolean isTopLevel() {
        return mIsTopLevel
    }

    @Override
    String getName() {
        return mName
    }

    @Override
    String getCategory() {
        return "Deutsche"
    }

    @Override
    String getShortDescription() {
        return mDescription
    }

    @Override
    String getImageUrl() {
        return mImageUrl != null ? mImageUrl : "http://www.planet-interview.de/wp-content/uploads/ARDlogo-300x300.jpg"
    }

    @Override
    String getBackgroundImageUrl() {
        return mBackgroundImageUrl != null ? mBackgroundImageUrl : "http://www.ard.de/image/55056/16x9/4788584338065477984/1280"
    }

    @Override
    boolean isUnavailableIn(String region) {
        return false
    }

    @Override
    String getPreferredRegion() {
        return null
    }

    @Override
    int getShieldLevel() {
        return 0
    }

    @Override
    ParentPuppet getParent() {
        return mParent
    }

    @Override
    PuppetIterator getRelated() {
        return null
    }

    @Override
    public String toString() {
        return mParent == null ? getName() : mParent.toString() + " < " + getName()
    }

    def static class ARDMediathekPuppetIterator extends PuppetIterator {

        def ArrayList<Puppet> mPuppets = new ArrayList<>()
        def int currentIndex = 0

        @Override
        boolean hasNext() {
            return currentIndex < mPuppets.size()
        }

        @Override
        void add(Puppet puppet) {
            mPuppets.add(puppet)
        }

        @Override
        Puppet next() {
            return mPuppets.get(currentIndex++)
        }

        @Override
        void remove() {
        }
    }

    def static class ARDMediathekSearchesPuppet extends ARDMediathekPuppet implements SearchesPuppet {

        static final String URL_TEMPLATE = "http://www.ardmediathek.de/tv/suche?searchText="

        public ARDMediathekSearchesPuppet(ParentPuppet parent) {
            super(parent, false, "Search", "Search ARD Mediathek", null, null, URL_TEMPLATE)
        }

        @Override
        public void setSearchQuery(String searchQuery) {
            mUrl = URL_TEMPLATE + URLEncoder.encode(searchQuery, "UTF-8")
        }
    }

    def static class ARDMediathekSourcesPuppet implements SourcesPuppet {

        def ParentPuppet mParent
        def boolean mIsLive
        def String mName
        def String mDescription
        def String mPublicationDate
        def String mImageUrl
        def String mBackgroundImageUrl
        def String mUrl

        def List<SourcesPuppet.SubtitleDescription> mSubtitles = new ArrayList<SourcesPuppet.SubtitleDescription>()

        ARDMediathekSourcesPuppet(ParentPuppet parent, boolean isLive, String name, String description, String publicationDate, String imageUrl, String backgroundImageUrl, String url) {
            mParent = parent
            mIsLive = isLive
            mName = name
            mDescription = description
            mPublicationDate = publicationDate
            mImageUrl = imageUrl
            mBackgroundImageUrl = backgroundImageUrl
            mUrl = url
        }

        @Override
        String getPublicationDate() {
            return mPublicationDate
        }

        @Override
        long getDuration() {
            -1
        }

        @Override
        SourcesPuppet.SourceIterator getSources() {
            return new ARDMediathekSourceIterator()
        }

        @Override
        boolean isLive() {
            return mIsLive
        }

        @Override
        List<SourcesPuppet.SubtitleDescription> getSubtitles() {
            return mSubtitles
        }

        @Override
        String getName() {
            mName
        }

        @Override
        String getCategory() {
            return null
        }

        @Override
        String getShortDescription() {
            return mDescription
        }

        @Override
        String getImageUrl() {
            return mImageUrl != null ? mImageUrl : mParent.getImageUrl()
        }

        @Override
        String getBackgroundImageUrl() {
            return mBackgroundImageUrl != null ? mBackgroundImageUrl : mParent.getBackgroundImageUrl()
        }

        @Override
        boolean isUnavailableIn(String region) {
            return region != 'de'
        }

        @Override
        String getPreferredRegion() {
            return 'de'
        }

        @Override
        int getShieldLevel() {
            return 0
        }

        @Override
        ParentPuppet getParent() {
            return mParent
        }

        @Override
        PuppetIterator getRelated() {
            return null
        }

        @Override
        String toString() {
            return mParent == null ? getName() : mParent.toString() + " < " + getName()
        }

        def class ARDMediathekSourceIterator implements SourcesPuppet.SourceIterator {

            def static final String URL_TEMPLATE = "http://www.ardmediathek.de/play/media/%s?devicetype=pc&features=flash"

            def ArrayList<SourcesPuppet.SourceDescription> mSources = null
            def int mCurrentIndex = 0

            @Override
            boolean hasNext() {
                if (mSources == null) {

                    mSources = new ArrayList<>()
                    def ArrayList<String> allUrls = []

                    if (mUrl.contains("kanal=")) {
                        def String page = new URL(mUrl).getText()
                        Matcher matcher = page =~ /\/play\/media\/(\d+)/
                        if (matcher.find()) {
                            mUrl = "documentId=" + matcher.group(1)
                        }
                    }

                    Matcher matcher = mUrl =~ /documentId=(\d+)/
                    if (matcher.find()) {
                        def String url = sprintf(URL_TEMPLATE, matcher.group(1))
                        def JSONObject json = new JSONObject(new URL(url).getText())
                        def JSONArray mediaArray = json.getJSONArray("_mediaArray")
                        for (int i = 0; i < mediaArray.length(); i++) {
                            def JSONArray streams = mediaArray.getJSONObject(i).getJSONArray("_mediaStreamArray")
                            for (int j = 0; j < streams.length(); j++) {
                                def JSONObject stream = streams.getJSONObject(j)
                                if (stream.get("_stream") instanceof String) {
                                    allUrls << stream.getString("_stream")
                                } else {
                                    JSONArray _stream = stream.getJSONArray("_stream")
                                    for (int k = 0; k < _stream.length(); k++) {
                                        allUrls << _stream.getString(k)
                                    }
                                }
                            }
                        }

                        allUrls.unique().each {
                            def SourcesPuppet.SourceDescription source = new SourcesPuppet.SourceDescription()
                            source.url = it
                            mSources.add(source)
                        }

                        if (json.has("_subtitleUrl")) {
                            SourcesPuppet.SubtitleDescription subtitle = new SourcesPuppet.SubtitleDescription()
                            subtitle.url = json.getString("_subtitleUrl")
                            subtitle.mime = "application/ttml+xml"
                            mSubtitles.add(subtitle)
                        }
                    }

                }
                return mCurrentIndex < mSources.size()
            }

            @Override
            SourcesPuppet.SourceDescription next() {
                return mSources.get(mCurrentIndex++)
            }

            @Override
            void remove() {
            }
        }
    }
}