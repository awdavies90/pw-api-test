package pw.api.test.bid

import spock.lang.*
import pw.api.test.BaseApiTest

class BaseBidTest extends BaseApiTest {
	
	def cleanup() {
		bidHelper.deleteAllBids()
	}
}
