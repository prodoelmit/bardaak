package org.prodoelmit

object Environment {
    val TELEGRAM_BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN")
    val DB_PATH = System.getenv("DB_PATH")
    val IMAGES_DIR = System.getenv("IMAGES_DIR")
    val ALGOLIA_APP_ID = System.getenv("ALGOLIA_APP_ID")
    val ALGOLIA_SEARCH_KEY = System.getenv("ALGOLIA_SEARCH_KEY")
    val ALGOLIA_WRITE_KEY = System.getenv("ALGOLIA_WRITE_KEY")
    val STORE_ARTICLE_REGEX = System.getenv("STORE_REGEX") ?: "\\d{3}\\.\\d{3}\\.\\d{2}"
    val STORE_BUTTON_TITLE = System.getenv("STORE_BUTTON_TITLE") ?: "IKEA"
    val STORE_BUTTON_URL = System.getenv("STORE_BUTTON_URL") ?: "https://www.ikea.com.cy/apotelesmata-anazitisis/?query={{article}}"
    val INDEX_NAME = System.getenv("INDEX_NAME") ?: "index_dev"
    val ALLOWED_USER_IDS = System.getenv("ALLOWED_USER_IDS") ?: ""
}