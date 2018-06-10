package pw.api.test.helpers

import spock.lang.*
import pw.api.test.BaseApiTest
import pw.api.test.utils.RandomUtils

class TrackHelper {
	
	List<Integer> trackIds = []
	List<Integer> customTrackIds = []
	BaseApiTest baseTest
	static artists = [
		'The Police',
		'Vampire Weekend',
		'Oasis',
		'Nirvana',
		'U2',
		'Kings of Leon',
		'Stevie Wonder',
		'REM',
		'Kasabian',
		'Metallica',
		'Guns N Roses',
		'Blink 182',
		'Donna Summer',
		'Offspring',
		'Animals',
		'Queens of the Stone Age',
		'Beetles',
		'Rolling Stones',
		'Blind Melon',
		'Adele',
		'Feeder',
		'Bloc Party',
		'Hard Fi',
		'Maximo Park',
		'Arcade Fire',
		'Reverand & The Makers',
		'Mumford & Sons',
		'David Bowie',
		'Ben Howard',
		'alt-j',
		'Arctic Monkeys',
		'Audioslave',
		'Average White Band',
		'Radiohead',
		'Nizlopi',
		'Green Day'
	]
	def tracks = [
		'Walking on the Moon',
		'A-Punk',
		'Whatever',
		'Lithium',
		'One',
		'Fans',
		'Superstition',
		'Orange Crush',
		'Fire',
		'Fuel',
		'Mr Browstone',
		'I Miss You',
		'Hot Stuff',
		'Original Prankster',
		'House of the Rising Sun',
		'Millionaire',
		'Yesterday',
		'Paint it Black',
		'No Rain',
		'Hello',
		'Just a Day',
		'Banquet',
		'Hard to Beat',
		'Books from Boxes',
		'Intervention',
		'Heavyweight Champion',
		'Lion Man',
		'Just Dance',
		'Conrad',
		'Tessalate',
		'When the Sun Goes Down',
		'Cochise',
		'Pick up the Pieces',
		'Street Spirit',
		'JCB Song',
		'Welcome to Paradice'
	]
	
	def TrackHelper(BaseApiTest baseTest) {
		this.baseTest = baseTest
	}
	
	def trackSearch(String searchText, String token) {
		baseTest.authToken = token
		def venues = baseTest.getCall("track/search?searchText=${URLEncoder.encode(searchText, 'UTF-8')}")
		if (venues) {
			venues.each {
				if (it?.id && !it.name?.contains('&')) {
					trackIds << it.id
				}
			}
		}
		venues
	}
	
	//To be finished
	def saveTrack(Map params, String token) {
		baseTest.authToken = token
		def response = baseTest.post('track/save', 'track/SaveTrack', params)
		if (response?.id) {
			customTrackIds << response.id
		}
		response
	}
	
	int getRandomTrackId(String token) {
		getRandomTrackIdOrCustomTrackId(trackIds) {
			baseTest.whileWithLimit(5, { trackIds.size() == 0 }) {
				def artist = getRandomArtist()
				trackSearch(artist, token)
			}
		}
	}
	
	String getRandomArtist() {
		RandomUtils.getRandomValueFrom(artists)
	}
	
	String getRandomTrack() {
		RandomUtils.getRandomValueFrom(tracks)
	}
	
	int getRandomCustomTrackId(String token) {
		getRandomTrackIdOrCustomTrackId(customTrackIds) {
			def params = [
				name:"Custom Venue Name ${new Date()}",
				address:"Custom Venue address ${new Date()}",
				postcode:randomPostcode
			]
			saveTrack(params, token)
		}
	}
	
	def deleteTrack(id) {
		deleteTrack(id, baseTest.adminUserToken)
	}
	
	def deleteTrack(id, String token) {
		baseTest.authToken = token
		def response = baseTest.delete("track/$id")
		if (response.success) {
			trackIds.removeIf { it == id }
			customTrackIds.removeIf { it == id }
		}
		response
	}
	
	def deleteAllCustomTracks() {
		List<Integer> trackIds = customTrackIds.collect { it }
		trackIds.each { trackId ->
			deleteTrack(trackId)
		}
		customTrackIds = []
	}
	
	private int getRandomTrackIdOrCustomTrackId(List<Integer> listOfIds, Closure action) {
		while (listOfIds.size() == 0) {
			action()
		}
		RandomUtils.getRandomValueFrom(listOfIds)
	}
}
