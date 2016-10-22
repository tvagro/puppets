package tv.puppetmaster.featured

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import tv.puppetmaster.data.i.*
import tv.puppetmaster.data.i.Puppet.PuppetIterator

import java.util.regex.Matcher

class ZDFMediathekPuppet implements InstallablePuppet {

    static final int VERSION_CODE = 5

    static final CHANNELS = [
            [
                    name:    "ZDF",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://zdf1314-lh.akamaihd.net/i/de14_v1@392878/master.m3u8",
                            "http://zdf1314-lh.akamaihd.net/i/de14_v1@392878/index_3056_av-p.m3u8",
                    ],
            ],
            [
                    name:    "ZDFneo",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://zdf1314-lh.akamaihd.net/i/de13_v1@392877/master.m3u8",
                    ],
            ],
            [
                    name:    "ZDF.kultur",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://zdf1112-lh.akamaihd.net/i/de11_v1@392881/master.m3u8",
                    ],
            ],
            [
                    name:    "ZDFinfo",
                    description:    "Livestream",
                    genres:         "ENTERTAINMENT",
                    urls:           [
                            "http://zdf1112-lh.akamaihd.net/i/de12_v1@392882/index_3056_av-b.m3u8?sd=10&dw=0&rebase=on",
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

    ZDFMediathekPuppet() {
        this(
                null,
                true,
                "ZDF Mediathek",
                "Erleben Sie das TV-Programm und alle Sendungen des Zweiten Deutschen Fernsehens ZDF ständig verfügbar und interaktiv - mit Serien, Shows & Dokumentationen.",
                null,
                null,
                "http://www.zdf.de/"
        )
    }

    ZDFMediathekPuppet(ParentPuppet parent, boolean isTopLevel, String name, String description, String imageUrl, String backgroundImageUrl, String url) {
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
        return new ZDFMediathekSearchesPuppet(this)
    }

    @Override
    SettingsPuppet getSettingsProvider() {
        return null
    }

    @Override
    int getFastlaneBackgroundColor() {
        return 0xFFAAA096
    }

    @Override
    int getSearchAffordanceBackgroundColor() {
        return 0xFFFB7D19
    }

    @Override
    int getSelectedBackgroundColor() {
        return 0xFFAAA096
    }

    @Override
    int getPlayerBackgroundColor() {
        return 0xFFFB7D19
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
        PuppetIterator children = new ZDFMediathekPuppetIterator()

        CHANNELS.each {
            children.add(new ZDFMediathekSourcesPuppet(this, it.name, it.description, null, -1, null, null, null, it.urls as String[]))
        }
        children.add(new ZDFMediathekXMLPuppet(this, false, "0-9", null, mImageUrl, mBackgroundImageUrl, "http://www.zdf.de/ZDFmediathek/xmlservice/web/sendungenAbisZ?characterRangeEnd=0-9&detailLevel=2&characterRangeStart=0-9"))
        ('A'..'Z').each {
            children.add(new ZDFMediathekXMLPuppet(this, false, it, null, mImageUrl, mBackgroundImageUrl, "http://www.zdf.de/ZDFmediathek/xmlservice/web/sendungenAbisZ?characterRangeEnd=${it}&detailLevel=2&characterRangeStart=${it}"))
        }
        [
                [
                        "title":    "STARTSEITE",
                        "url":      "http://www.zdf.de/ZDFmediathek/xmlservice/web/aktuellste?offset=0&maxLength=10&id=_STARTSEITE",
                ],
                [
                        "title":    "GLOBAL",
                        "url":      "http://www.zdf.de/ZDFmediathek/xmlservice/web/meistGesehen?offset=0&maxLength=10&id=_GLOBAL",
                ],
                [
                        "title":    "NACHRICHTEN",
                        "url":      "http://www.zdf.de/ZDFmediathek/xmlservice/web/meistGesehen?offset=0&maxLength=10&id=_NACHRICHTEN",
                ],
                [
                        "title":    "RUBRIKEN",
                        "url":      "http://www.zdf.de/ZDFmediathek/xmlservice/web/rubriken",
                ],
                [
                        "title":    "THEMEN",
                        "url":      "http://www.zdf.de/ZDFmediathek/xmlservice/web/themen",
                ],
        ].each {
            children.add(new ZDFMediathekXMLPuppet(this, true, it.title, null, mImageUrl, mBackgroundImageUrl, it.url))
        }
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
        return mImageUrl != null ? mImageUrl : "http://www.konsolen-zone.de/Ebay/Fire_TV/Ebay/Mediatheken/iconZDF.jpg"
    }

    @Override
    String getBackgroundImageUrl() {
        return mBackgroundImageUrl != null ? mBackgroundImageUrl : "https://theinad.com/wp-content/uploads/2015/01/zdf-mediathek.jpg"
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

    def static class ZDFMediathekPuppetIterator extends PuppetIterator {

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

    def static class ZDFMediathekSearchesPuppet extends ZDFMediathekXMLPuppet implements SearchesPuppet {

        static final String URL_TEMPLATE = "http://www.zdf.de/ZDFmediathek/xmlservice/web/detailsSuche?offset=0&maxLength=20&searchString="

        public ZDFMediathekSearchesPuppet(ParentPuppet parent) {
            super(parent, false, "Suche", "Suche ZDF Mediathek", null, null, URL_TEMPLATE)
        }

        @Override
        public void setSearchQuery(String searchQuery) {
            mUrl = URL_TEMPLATE + URLEncoder.encode(searchQuery, "UTF-8")
        }
    }

    def static class ZDFMediathekXMLPuppet extends ZDFMediathekPuppet {

        ZDFMediathekXMLPuppet(ParentPuppet parent, boolean isTopLevel, String name, String description, String imageUrl, String backgroundImageUrl, String url) {
            super(parent, isTopLevel, name, description, imageUrl, backgroundImageUrl, url)
        }

        @Override
        PuppetIterator getChildren() {
            PuppetIterator children = new ZDFMediathekPuppetIterator()

            def Document feed = Jsoup.parse(new URL(mUrl).getText(), "", Parser.xmlParser())
            def int numItems = 0
            feed.select("teaser").each {
                def String assetId = it.select("assetId").first().text()
                def String imageUrl = it.select("teaserimage").size() > 1 ? it.select("teaserimage").get(1).text() : it.select("teaserimage").first().text()
                if (it.select("type").first().text() in ["video", "livevideo"]) {
                    children.add(new ZDFMediathekSourcesPuppet(
                            this,
                            it.select("title").first().text(),
                            it.select("detail").first().text(),
                            it.select("airtime").first().text(),
                            it.select("lengthSec") ? Long.parseLong(it.select("lengthSec").first().text()) * 1000 : -1,
                            imageUrl,
                            it.select("teaserimage").last().text(),
                            assetId,
                            null
                    ))
                } else {
                    children.add(new ZDFMediathekXMLPuppet(
                            this,
                            false,
                            it.select("title").first().text(),
                            it.select("detail").first().text(),
                            imageUrl,
                            it.select("teaserimage").last().text(),
                            "http://www.zdf.de/ZDFmediathek/xmlservice/web/aktuellste?id=${assetId}&offset=0&maxLength=25"
                    ))
                }
                numItems++
            }

            if (numItems > 0 && mUrl.contains("offset")) {
                Matcher matcher = mUrl =~ /offset=(\d+)&maxLength=(\d+)/
                if (matcher.find()) {
                    int offset = Integer.parseInt(matcher.group(1)) + 50
                    int maxLength = Integer.parseInt(matcher.group(2))
                    if (maxLength <= numItems) {
                        mUrl = mUrl.replace("offset=" + matcher.group(1), "offset=" + offset)
                        if (maxLength < 50) {
                            mUrl = mUrl.replace("maxLength=" + maxLength, "maxLength=50")
                        }

                        children.add(new ZDFMediathekXMLPuppet(
                                this,
                                false,
                                ">>",
                                mDescription,
                                mImageUrl,
                                mBackgroundImageUrl,
                                mUrl
                        ))
                    }
                }
            }
            return children
        }
    }

    def static class ZDFMediathekSourcesPuppet implements SourcesPuppet {

        def static final String URL_TEMPLATE = "http://www.zdf.de/ZDFmediathek/xmlservice/web/beitragsDetails?id="

        def ParentPuppet mParent
        def String mName
        def String mDescription
        def String mPublicationDate
        def long mDuration
        def String mImageUrl
        def String mBackgroundImageUrl
        def String mId
        def String[] mLiveUrls

        def List<SourcesPuppet.SubtitleDescription> mSubtitles = new ArrayList<SourcesPuppet.SubtitleDescription>()

        ZDFMediathekSourcesPuppet(ParentPuppet parent, String name, String description, String publicationDate, long duration, String imageUrl, String backgroundImageUrl, String id, String[] liveUrls) {
            mParent = parent
            mName = name
            mDescription = description
            mPublicationDate = publicationDate
            mDuration = duration
            mImageUrl = imageUrl
            mBackgroundImageUrl = backgroundImageUrl
            mId = id
            mLiveUrls = liveUrls
        }

        @Override
        String getPublicationDate() {
            return mPublicationDate
        }

        @Override
        long getDuration() {
            mDuration
        }

        @Override
        SourcesPuppet.SourceIterator getSources() {
            return new ZDFMediathekSourceIterator()
        }

        @Override
        boolean isLive() {
            return mId == null
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
            if (region != 'de') {
                try {
                    def Document feed = Jsoup.parse(new URL(URL_TEMPLATE + mId).getText(), "", Parser.xmlParser())
                    if (feed.select("geolocation").first().text().trim() == 'none') {
                        return true
                    }
                } catch (ignore) {}
            }
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

        def class ZDFMediathekSourceIterator implements SourcesPuppet.SourceIterator {

            def ArrayList<SourcesPuppet.SourceDescription> mSources = null
            def int mCurrentIndex = 0

            @Override
            boolean hasNext() {
                if (mSources == null && mId == null) {

                    mSources = new ArrayList<>()

                    mLiveUrls.each {
                        def SourcesPuppet.SourceDescription source = new SourcesPuppet.SourceDescription()
                        source.url = it
                        mSources.add(source)
                    }

                } else if (mSources == null) {

                    mSources = new ArrayList<>()

                    def String page = new URL(URL_TEMPLATE + mId).getText()

                    def Document feed = Jsoup.parse(page, "", Parser.xmlParser())
                    feed.select("formitaet").each {
                        def SourcesPuppet.SourceDescription source = new SourcesPuppet.SourceDescription()
                        source.url = it.getElementsByTag("url").first().text()
                        try {
                            source.width = it.getElementsByTag("width").first().text()
                            source.bitrate = Long.parseLong(it.getElementsByTag("videoBitrate").first().text())
                        } catch (ignore) {
                        }
                        mSources.add(source)
                    }

                    Collections.sort(mSources, new Comparator<SourcesPuppet.SourceDescription>() {
                        @Override
                        public int compare(SourcesPuppet.SourceDescription lhs, SourcesPuppet.SourceDescription rhs) {
                            // Reverse sort so higher quality is tried first
                            if (rhs.url.endsWith(".m3u8") && !lhs.url.endsWith(".m3u8")) {
                                return 1
                            } else if (lhs.url.endsWith(".m3u8") && !rhs.url.endsWith(".m3u8")) {
                                return -1
                            }
                            return Long.compare(rhs.bitrate, lhs.bitrate)
                        }
                    })
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