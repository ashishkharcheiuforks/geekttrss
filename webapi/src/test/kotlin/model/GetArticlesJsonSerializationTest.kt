/*
 * Geekttrss is a RSS feed reader application on the Android Platform.
 *
 * Copyright (C) 2017-2019 by Frederic-Charles Barthelery.
 *
 * This file is part of Geekttrss.
 *
 * Geekttrss is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Geekttrss is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Geekttrss.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.geekorum.ttrss.webapi.model

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlin.test.Test

@UseExperimental(UnstableDefault::class)
class GetArticlesJsonSerializationTest {

    @Test
    fun testThatGetArticlesRequestPayloadDoCorrectJson() {
        val payload = GetArticlesRequestPayload(
            feedId = 256,
            viewMode = GetArticlesRequestPayload.ViewMode.ALL_ARTICLES,
            showContent = true,
            showExcerpt = false,
            skip = 10,
            sinceId = 200,
            limit = 10,
            orderBy = GetArticlesRequestPayload.SortOrder.TITLE
        ).apply {
            sessionId = "SESSION_ID"
        }
        val serializer = getSerializer<GetArticlesRequestPayload>()
        val result = Json.stringify(serializer, payload)
        assertThat(result).isEqualTo("""
            {"sid":"SESSION_ID","feed_id":256,"view_mode":"ALL_ARTICLES","show_content":true,"show_excerpt":false,"skip":10,"since_id":200,"limit":10,"order_by":"TITLE","op":"getHeadlines"}
        """.trimIndent())
    }

    @Test
    fun testThatGetArticlesResponsePayloadDoLoadCorrectly() {
        val jsonString = """
        {
          "seq": 2,
          "status": 1,
          "content": [
            {
              "id": 560752,
              "unread": true,
              "marked": false,
              "published": false,
              "updated": 1541045992,
              "is_updated": false,
              "title": "TMZ on TV 2018 10 30 480p x264-mSD",
              "link": "https:\/\/eztv.re\/ep\/1284471\/tmz-on-tv-2018-10-30-480p-x264-msd\/",
              "feed_id": "82",
              "tags": [
                "tv"
              ],
              "excerpt": "",
              "content": "",
              "labels": [],
              "feed_title": "ezRSS - Latest torrent releases",
              "comments_count": 0,
              "comments_link": "",
              "always_display_attachments": false,
              "author": "",
              "score": 0,
              "note": null,
              "lang": ""
            },
            {
              "id": 560718,
              "unread": false,
              "marked": false,
              "published": false,
              "updated": 1541039212,
              "is_updated": false,
              "title": "Images du jour 31\/10\/18 \u2013 Martinique \u2013 Philippot-",
              "link": "http:\/\/www.bondamanjak.com\/images-jour-31-10-18-martinique-philippot\/",
              "feed_id": "191",
              "tags": [
                "a la une",
                "aujourdhui en martinique",
                "breaking news",
                "martinique",
                "b\u00e9atrice bellay",
                "florian philippot",
                "marine dewo",
                "socialiste"
              ],
              "excerpt": "&hellip;",
              "content": "<html><body><header><h1 itemprop=\"mainEntityOfPage\">Images du jour 31\/10\/18 &ndash; Martinique &ndash; Philippot-<\/h1>\r\n        <p>\r\n        <span itemprop=\"dateCreated\">octobre 31st, 2018<\/span>                                    <\/p>\r\n        <\/header><div><p><img src=\"https:\/\/tinyrss.elyzion.net\/public.php?op=cached_image&amp;hash=daff495cb40939033ed3cbd5c20d0a8c7d37fc07\" alt=\"\" data-recalc-dims=\"1\"><img src=\"https:\/\/tinyrss.elyzion.net\/public.php?op=cached_image&amp;hash=473855ee3a04935e8c50c7f7ada769c2b0521bbe\" alt=\"\" data-recalc-dims=\"1\"><br>\nCe jour en #Martinique, une pinc&eacute;e de militants-es de Marine D&egrave;wo ont emp&ecirc;ch&eacute; la venue du d&eacute;put&eacute; europ&eacute;en Florian Philippot dans les studios de &laquo;&nbsp;RFO&nbsp;&raquo; Martinique La 1&egrave;re.&nbsp; On note la pr&eacute;sence d&eacute;termin&eacute;e et clairement affich&eacute;e de B&eacute;atrice Bellay la premi&egrave;re secr&eacute;taire de la F&eacute;d&eacute;ration socialiste de l&rsquo;&icirc;le.&nbsp; Comme quoi le kumquat.<\/p>\n<div><div data-a2a-url=\"http:\/\/www.bondamanjak.com\/images-jour-31-10-18-martinique-philippot\/\" data-a2a-title=\"Images du jour 31\/10\/18 &ndash; Martinique &ndash; Philippot-\"><a href=\"https:\/\/www.addtoany.com\/add_to\/facebook?linkurl=http%3A%2F%2Fwww.bondamanjak.com%2Fimages-jour-31-10-18-martinique-philippot%2F&amp;linkname=Images%20du%20jour%2031%2F10%2F18%20%E2%80%93%20Martinique%20%E2%80%93%20Philippot-\" title=\"Facebook\" rel=\"noreferrer\" target=\"_blank\"><\/a><a href=\"https:\/\/www.addtoany.com\/add_to\/twitter?linkurl=http%3A%2F%2Fwww.bondamanjak.com%2Fimages-jour-31-10-18-martinique-philippot%2F&amp;linkname=Images%20du%20jour%2031%2F10%2F18%20%E2%80%93%20Martinique%20%E2%80%93%20Philippot-\" title=\"Twitter\" rel=\"noreferrer\" target=\"_blank\"><\/a><a href=\"https:\/\/www.addtoany.com\/add_to\/google_plus?linkurl=http%3A%2F%2Fwww.bondamanjak.com%2Fimages-jour-31-10-18-martinique-philippot%2F&amp;linkname=Images%20du%20jour%2031%2F10%2F18%20%E2%80%93%20Martinique%20%E2%80%93%20Philippot-\" title=\"Google+\" rel=\"noreferrer\" target=\"_blank\"><\/a><a href=\"https:\/\/www.addtoany.com\/add_to\/whatsapp?linkurl=http%3A%2F%2Fwww.bondamanjak.com%2Fimages-jour-31-10-18-martinique-philippot%2F&amp;linkname=Images%20du%20jour%2031%2F10%2F18%20%E2%80%93%20Martinique%20%E2%80%93%20Philippot-\" title=\"WhatsApp\" rel=\"noreferrer\" target=\"_blank\"><\/a><a href=\"https:\/\/www.addtoany.com\/share\" rel=\"noreferrer\" target=\"_blank\"><\/a><\/div><\/div><\/div>\r\n<!-- start:tags -->\r\n<!-- end:tags --><!-- start:article footer --><!-- end:article footer --><div itemprop=\"publisher\" itemscope=\"\" itemtype=\"https:\/\/schema.org\/Organization\">\r\n    <div itemprop=\"logo\" itemscope=\"\" itemtype=\"https:\/\/schema.org\/ImageObject\">\r\n      <\/div>\r\n    <\/div>\r\n\r\n\r<\/body><\/html>\n",
              "labels": [],
              "feed_title": "Bondamanjak",
              "comments_count": 0,
              "comments_link": "http:\/\/www.bondamanjak.com\/images-jour-31-10-18-martinique-philippot\/#respond",
              "always_display_attachments": true,
              "author": "bondamanjak",
              "score": 0,
              "note": null,
              "lang": ""
            },
            {
              "id": 560198,
              "unread": false,
              "marked": false,
              "published": false,
              "updated": 1541039212,
              "is_updated": false,
              "title": "Some article from apiLevel 14 and tt-rss version 19.8",
              "link": "https:\/\/discourse.tt-rss.org\/t\/blacklisted-by-feedburner-ever-happened-to-anyone\/2677\/9",
              "feed_id": 4,
              "tags": [""],
              "excerpt": "",
              "content": "<html><body><header><h1>Some article from apiLevel 14 and tt-rss version 19.8<\/h1><\/header><\/body><\/html>",
              "labels": [],
              "feed_title": "Tiny Tiny RSS: Forum",
              "comments_count": 0,
              "comments_link": "",
              "always_display_attachments": true,
              "author": "anonymous",
              "score": 0,
              "note": null,
              "lang": "",
              "flavor_image":"https:\/\/discourse.tt-rss.org\/user_avatar\/discourse.tt-rss.org\/fox\/40\/889_2.png",
              "flavor_stream":""
            }
          ]
        }
""".trimIndent()
        val serializer = getSerializer<ListResponsePayload<Headline>>()
        val result = Json.parse(serializer, jsonString)
        val expected = ListResponsePayload<Headline>(
            sequence = 2,
            status = 1,
            content = ListContent(listOf(
                Headline(id = 560752, unread = true, marked = false, published = false, lastUpdatedTimestamp = 1541045992,
                    isUpdated = false, title = "TMZ on TV 2018 10 30 480p x264-mSD",
                    link = "https://eztv.re/ep/1284471/tmz-on-tv-2018-10-30-480p-x264-msd/",
                    feedId = 82, tags = listOf("tv"), excerpt = "", content = "", labels = emptyList(),
                    feedTitle = "ezRSS - Latest torrent releases",
                    nbComments = 0, commentsLink = "", alwaysDisplayAttachment = false, author = "",
                    score = 0, note = null, lang = ""),
                Headline(id = 560718, unread = false, marked = false, published = false, lastUpdatedTimestamp = 1541039212,
                    isUpdated = false, title = "Images du jour 31/10/18 \u2013 Martinique \u2013 Philippot-",
                    link = "http://www.bondamanjak.com/images-jour-31-10-18-martinique-philippot/",
                    feedId = 191, tags = listOf(
                        "a la une",
                        "aujourdhui en martinique",
                        "breaking news",
                        "martinique",
                        "b\u00e9atrice bellay",
                        "florian philippot",
                        "marine dewo",
                        "socialiste"),
                    excerpt = "&hellip;",
                    content = "<html><body><header><h1 itemprop=\"mainEntityOfPage\">Images du jour 31/10/18 &ndash; Martinique &ndash; Philippot-</h1>\r\n        <p>\r\n        <span itemprop=\"dateCreated\">octobre 31st, 2018</span>                                    </p>\r\n        </header><div><p><img src=\"https://tinyrss.elyzion.net/public.php?op=cached_image&amp;hash=daff495cb40939033ed3cbd5c20d0a8c7d37fc07\" alt=\"\" data-recalc-dims=\"1\"><img src=\"https://tinyrss.elyzion.net/public.php?op=cached_image&amp;hash=473855ee3a04935e8c50c7f7ada769c2b0521bbe\" alt=\"\" data-recalc-dims=\"1\"><br>\nCe jour en #Martinique, une pinc&eacute;e de militants-es de Marine D&egrave;wo ont emp&ecirc;ch&eacute; la venue du d&eacute;put&eacute; europ&eacute;en Florian Philippot dans les studios de &laquo;&nbsp;RFO&nbsp;&raquo; Martinique La 1&egrave;re.&nbsp; On note la pr&eacute;sence d&eacute;termin&eacute;e et clairement affich&eacute;e de B&eacute;atrice Bellay la premi&egrave;re secr&eacute;taire de la F&eacute;d&eacute;ration socialiste de l&rsquo;&icirc;le.&nbsp; Comme quoi le kumquat.</p>\n<div><div data-a2a-url=\"http://www.bondamanjak.com/images-jour-31-10-18-martinique-philippot/\" data-a2a-title=\"Images du jour 31/10/18 &ndash; Martinique &ndash; Philippot-\"><a href=\"https://www.addtoany.com/add_to/facebook?linkurl=http%3A%2F%2Fwww.bondamanjak.com%2Fimages-jour-31-10-18-martinique-philippot%2F&amp;linkname=Images%20du%20jour%2031%2F10%2F18%20%E2%80%93%20Martinique%20%E2%80%93%20Philippot-\" title=\"Facebook\" rel=\"noreferrer\" target=\"_blank\"></a><a href=\"https://www.addtoany.com/add_to/twitter?linkurl=http%3A%2F%2Fwww.bondamanjak.com%2Fimages-jour-31-10-18-martinique-philippot%2F&amp;linkname=Images%20du%20jour%2031%2F10%2F18%20%E2%80%93%20Martinique%20%E2%80%93%20Philippot-\" title=\"Twitter\" rel=\"noreferrer\" target=\"_blank\"></a><a href=\"https://www.addtoany.com/add_to/google_plus?linkurl=http%3A%2F%2Fwww.bondamanjak.com%2Fimages-jour-31-10-18-martinique-philippot%2F&amp;linkname=Images%20du%20jour%2031%2F10%2F18%20%E2%80%93%20Martinique%20%E2%80%93%20Philippot-\" title=\"Google+\" rel=\"noreferrer\" target=\"_blank\"></a><a href=\"https://www.addtoany.com/add_to/whatsapp?linkurl=http%3A%2F%2Fwww.bondamanjak.com%2Fimages-jour-31-10-18-martinique-philippot%2F&amp;linkname=Images%20du%20jour%2031%2F10%2F18%20%E2%80%93%20Martinique%20%E2%80%93%20Philippot-\" title=\"WhatsApp\" rel=\"noreferrer\" target=\"_blank\"></a><a href=\"https://www.addtoany.com/share\" rel=\"noreferrer\" target=\"_blank\"></a></div></div></div>\r\n<!-- start:tags -->\r\n<!-- end:tags --><!-- start:article footer --><!-- end:article footer --><div itemprop=\"publisher\" itemscope=\"\" itemtype=\"https://schema.org/Organization\">\r\n    <div itemprop=\"logo\" itemscope=\"\" itemtype=\"https://schema.org/ImageObject\">\r\n      </div>\r\n    </div>\r\n\r\n\r</body></html>\n",
                    labels = emptyList(),
                    feedTitle = "Bondamanjak",
                    nbComments = 0,
                    commentsLink = "http://www.bondamanjak.com/images-jour-31-10-18-martinique-philippot/#respond",
                    alwaysDisplayAttachment = true, author = "bondamanjak",
                    score = 0, note = null, lang = ""),
                Headline(id = 560198, unread = false, marked = false, published = false, lastUpdatedTimestamp = 1541039212,
                    isUpdated = false, title = "Some article from apiLevel 14 and tt-rss version 19.8",
                    link = "https://discourse.tt-rss.org/t/blacklisted-by-feedburner-ever-happened-to-anyone/2677/9",
                    feedId = 4, tags = listOf(""),
                    excerpt = "",
                    content = "<html><body><header><h1>Some article from apiLevel 14 and tt-rss version 19.8</h1></header></body></html>",
                    labels = emptyList(),
                    feedTitle = "Tiny Tiny RSS: Forum",
                    nbComments = 0,
                    commentsLink = "",
                    alwaysDisplayAttachment = true,
                    author = "anonymous",
                    score = 0, note = null, lang = "",
                    flavorImage = "https://discourse.tt-rss.org/user_avatar/discourse.tt-rss.org/fox/40/889_2.png",
                    flavorStream = "")

            ))
        )
        assertThat(result.sequence).isEqualTo(expected.sequence)
        assertThat(result.status).isEqualTo(expected.status)
        assertThat(result.content.list).containsExactlyElementsIn(expected.content.list)
    }

    @Test
    fun testThatGetArticlesResponsePayloadWithErrorDoLoadCorrectly() {
        val jsonString = """
            {
              "seq": 2,
              "status": 1,
              "content": {"error":"NOT_LOGGED_IN"}
            }
        """.trimIndent()
        val serializer = getSerializer<ListResponsePayload<Headline>>()
        val result = Json.parse(serializer, jsonString)
        val expected = ListResponsePayload<Headline>(
            sequence = 2,
            status = 1,
            content = ListContent(error = Error.NOT_LOGGED_IN)
        )
        assertThat(result.sequence).isEqualTo(expected.sequence)
        assertThat(result.status).isEqualTo(expected.status)
        assertThat(result.content.error).isEqualTo(expected.content.error)
    }
}