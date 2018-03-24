package pw.api.test.venue

import pw.api.test.BaseApiTest
import pw.api.test.helpers.VenueHelper
import spock.lang.*

class SaveVenue extends BaseVenueTest {
	
	def "Save Venue"() {
		
		given:'A valid custom venue is to be saved'
			def params = [
				name:'New Custom Venue',
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'VE1 2NU'
			]
					
		when:'The venue is saved'
			def saveResponse = venueHelper.saveVenue(params, individualUserToken)
			def getVenueResponse = venueHelper.getVenue(saveResponse?.id, individualUserToken)
		
		then:'The venue is correctly saved'
			saveResponse.id != null
			saveResponse.address == params.address
			saveResponse.dateCreated != null && saveResponse.dateCreated != ''
			saveResponse.dateUpdated != null && saveResponse.dateUpdated != ''
			saveResponse.googleMapsPlaceId == null
			saveResponse.isCustomAddress == true
			saveResponse.location == null
			saveResponse.name == params.name
			saveResponse.postcode == params.postcode
			saveResponse.user?.id == userHelper.getUserIdByToken(individualUserToken)
			
			getVenueResponse.id == saveResponse.id
			getVenueResponse.address == params.address
			getVenueResponse.dateCreated != null && saveResponse.dateCreated != ''
			getVenueResponse.dateUpdated != null && saveResponse.dateUpdated != ''
			getVenueResponse.googleMapsPlaceId == null
			getVenueResponse.isCustomAddress == true
			getVenueResponse.location == null
			getVenueResponse.name == params.name
			getVenueResponse.postcode == params.postcode
			getVenueResponse.user?.id == userHelper.getUserIdByToken(individualUserToken)
	}
}
