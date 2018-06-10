package pw.api.test.track

import pw.api.test.BaseApiTest
import pw.api.test.helpers.TrackHelper
import pw.api.test.utils.RandomUtils
import spock.lang.*

class TrackSearch extends BaseTrackTest {
	
	def "Track Search By Artist"() {
		
		given:'A Track is searched for by artist name'
			def upperCase = RandomUtils.trueOrFalse()
			def artistName = trackHelper.getRandomArtist()
			def searchText = upperCase ? artistName.toUpperCase() : artistName.toLowerCase()
					
		when:'The search is performed'
			def response = trackHelper.trackSearch(searchText, individualUserToken)
		
		then:'Tracks belonging to that artist are correctly returned'
			verifyTracks(searchText, response)
	}
	
	def "Track Search By Part Artist"() {
		
		given:'A Track is searched for by part of an artist name'
			def searchText
			while (!searchText) {
				def artistName = trackHelper.getRandomArtist()
				def partArtistName = artistName[0..(artistName.size() / 2)].trim()
				searchText = (partArtistName.size() > 2) ? partArtistName : null
			}
					
		when:'The search is performed'
			def response = trackHelper.trackSearch(searchText, individualUserToken)
		
		then:'Tracks belonging to that artist are correctly returned'
			verifyTracks(searchText, response)
	}
	
	def "Track Search By Track Name"() {
		
		given:'A Track is searched for by track name'
			def upperCase = RandomUtils.trueOrFalse()
			def trackName = trackHelper.getRandomTrack()
			def searchText = upperCase ? trackName.toUpperCase() : trackName.toLowerCase()
					
		when:'The search is performed'
			def response = trackHelper.trackSearch(searchText, individualUserToken)
		
		then:'Tracks containing the search text are correctly returned'
			verifyTracks(searchText, response)
	}
	
	def "Track Search By Part Track Name"() {
		
		given:'A Track is searched for by part of an track name'
			def searchText
			while (!searchText) {
				def trackName = trackHelper.getRandomTrack()
				def partTrackName = trackName[0..(trackName.size() / 2)].trim()
				searchText = (partTrackName.size() > 2) ? partTrackName : null
			}
					
		when:'The search is performed'
			def response = trackHelper.trackSearch(searchText, individualUserToken)
		
		then:'Tracks containing the search text are correctly returned'
			verifyTracks(searchText, response)
	}
	
	def verifyTracks(String searchText, response) {
		response.removeAll { it.responseCode }
		assert response.size() > 0
		assert response.every { it.id != null }
		assert response.every { it.artist != null}
		assert response.every { it.artist.id != null}
		
		response.each {
			def combinedTrackAndArtist = removeSpecialCharsAndSpacesFromString("${it.name}${it.artist}").toLowerCase()
			def formattedSearchText = removeSpecialCharsAndSpacesFromString(searchText).toLowerCase()
			assert combinedTrackAndArtist.contains(formattedSearchText)
		}
		
		//assert response.every { it.dateCreated != null && it.dateCreated != '' }
		//assert response.every { it.dateUpdated != null && it.dateUpdated != '' }
		true
	}
	
	private String removeSpecialCharsAndSpacesFromString(String text) {
		text.replace('.', '')
			.replace('-', '')
			.replace('(', '')
			.replace(')', '')
			.replace('\'', '')
			.replace('.', '')
			.replace(' ', '')
	}
	
	def verifyCustomTracks(response) {
		response.removeAll { it.responseCode }
		assert response.size() == 1
		assert response[0].id != null
		assert response[0].address == customTrackParams.address
		assert response[0].dateCreated != null && response[0].dateCreated != ''
		assert response[0].dateUpdated != null && response[0].dateUpdated != ''
		assert response[0].googleMapsPlaceId == null
		assert response[0].isCustomAddress == true
		assert response[0].location == null
		assert response[0].name == customTrackParams.name
		assert response[0].postcode == customTrackParams.postcode
		assert response[0].user?.id == userHelper.getUserIdByToken(individualUserToken)
		true
	}
}
