package pw.api.test.venue

import pw.api.test.BaseApiTest
import pw.api.test.helpers.VenueHelper
import pw.api.test.utils.Templater
import spock.lang.*

class SaveVenue extends BaseVenueTest {
	
	@Unroll
	def "Save Venue"() {
		
		given:'A valid custom venue is to be saved'
			def params = [
				name:name,
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:postcode
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
			saveResponse.postcode == params.postcode.toUpperCase()
			saveResponse.user?.id == userHelper.getUserIdByToken(individualUserToken)
			
			getVenueResponse.id == saveResponse.id
			getVenueResponse.address == params.address
			getVenueResponse.dateCreated != null && saveResponse.dateCreated != ''
			getVenueResponse.dateUpdated != null && saveResponse.dateUpdated != ''
			getVenueResponse.googleMapsPlaceId == null
			getVenueResponse.isCustomAddress == true
			getVenueResponse.location == null
			getVenueResponse.name == params.name
			getVenueResponse.postcode == params.postcode.toUpperCase()
			getVenueResponse.user?.id == userHelper.getUserIdByToken(individualUserToken)
		
		where:'The following inputs are used'
			name			   | postcode  | description
			'New Custom Venue' | 'VE1 2NU' | ''
			'Venue with an &'  | 'VE2 3NU' | 'Venue name contains &'
			'Lovercase Pstcde' | 've3 4nu' | 'Lowercase postcode'
	}
}
