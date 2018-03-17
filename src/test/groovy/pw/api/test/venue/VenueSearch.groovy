package pw.api.test.venue

import pw.api.test.BaseApiTest
import pw.api.test.helpers.VenueHelper
import pw.api.test.utils.RandomUtils
import spock.lang.*

class VenueSearch extends BaseApiTest {
	
	def "Get Venue from Google Maps - By Postcode"() {
		
		given:'A venue is searched for by postcode'
			def postcodes = [
				'NG17 9JQ',
				'ME7 3DD',
				'PR4 6NG',
				'PL20 7RU',
				'TA24 5DL',
				'DY12 1LA'
			]
			def postcode = RandomUtils.getRandomValueFrom(postcodes)
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(postcode, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			response.size() > 0
			response.every { it.id != null }
			response.every { it.address != null && it.address != '' }
			response.every { it.dateCreated != null && it.dateCreated != '' }
			response.every { it.dateUpdated != null && it.dateUpdated != '' }
			response.every { it.googleMapsPlaceId != null && it.googleMapsPlaceId != '' }
			response.every { it.isCustomAddress == false }
			response.every { it.location.id != null }
			response.every { it.location.lat != null }
			response.every { it.location.lng != null }
			response.every { it.name != null }
	}
	
	@IgnoreRest
	def "Get Custom Venue - By Postcode"() {
		
		given:'A venue is searched for by postcode'
			def venueParams = [
				name:'My New Venue',
				address:'Woodside Rd, Woodlands, Doncaster',
				postcode:'VE1 2NU'	
			]
			venueHelper.saveVenue(venueParams, individualUserToken)
					
		when:'The search is performed'
			def response = venueHelper.venueSearch(venueParams.postcode, individualUserToken)
		
		then:'Nearby venues are correctly returned'
			response.size() == 1
			response[0].id != null
			response[0].address == venueParams.address
			response[0].dateCreated != null && response[0].dateCreated != ''
			response[0].dateUpdated != null && response[0].dateUpdated != ''
			response[0].googleMapsPlaceId == null
			response[0].isCustomAddress == true
			response[0].location == null
			response[0].name == venueParams.name
			response[0].postcode == venueParams.postcode
			response[0].user?.id == userHelper.getUserIdByToken(individualUserToken)
	}
}
