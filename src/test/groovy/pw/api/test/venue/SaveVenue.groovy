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
			verifyCustomVenue(params, individualUserToken, saveResponse)
			verifyCustomVenue(params, individualUserToken, getVenueResponse)
		
		where:'The following inputs are used'
			name			   | postcode  | description
			'New Custom Venue' | 'VE1 2NU' | ''
			'Venue with an &'  | 'VE2 3NU' | 'Venue name contains &'
			'Lovercase Pstcde' | 've3 4nu' | 'Lowercase postcode'
	}
	
	def "Save Venue - Same Custom Venue for Two Users"() {
		
		given:'A custom venue is to be saved'
			def params = [
				name:'Same Name Venue',
				address:'Nowhere Street, Nowherarea, Nowherecity',
				postcode:'XX1 2XX'
			]
					
		when:'The same venue is saved for two different users'
			def saveResponse1 = venueHelper.saveVenue(params, individualUserToken)
			def getVenueResponse1 = venueHelper.getVenue(saveResponse1?.id, individualUserToken)
			def saveResponse2 = venueHelper.saveVenue(params, individualUserToken2)
			def getVenueResponse2 = venueHelper.getVenue(saveResponse2?.id, individualUserToken2)
		
		then:'Both venues are correctly saved'
			verifyCustomVenue(params, individualUserToken, saveResponse1)
			verifyCustomVenue(params, individualUserToken, getVenueResponse1)
			verifyCustomVenue(params, individualUserToken2, saveResponse2)
			verifyCustomVenue(params, individualUserToken2, getVenueResponse2)
	}
}
