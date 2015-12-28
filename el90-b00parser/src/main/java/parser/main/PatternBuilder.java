package parser.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import parser.dataobjects.B00Note;
import electone.constants.DrumInstrument;
import electone.dataobjects.Channel;
import electone.dataobjects.Notes;
import electone.dataobjects.Pattern;
import electone.dataobjects.PatternConstants;
import electone.dataobjects.PatternIdent;
import electone.dataobjects.Quantization;
import electone.dataobjects.TimeSignature;
import electone.dataobjects.Track;
import electone.dataobjects.Volume;

public class PatternBuilder {

	private Map<Channel, Notes> channelMap = new HashMap<>();
	private List<DrumInstrument> channelInstruments;
	private List<Channel> channels;
	private PatternIdent patternIdent;
	private TimeSignature quarterTime;

	public PatternBuilder(List<DrumInstrument> channelInstruments, PatternIdent patternIdent, TimeSignature quarterTime) {
		this.channelInstruments = channelInstruments;
		this.patternIdent = patternIdent;
		this.quarterTime = quarterTime;

		initChannels();
		initChannelMap();
	}

	private void initChannelMap() {
		channels.forEach(channel -> channelMap.put(channel, new Notes()));
	}

	private void initChannels() {
		channels = IntStream.range(0, this.channelInstruments.size())
				.boxed()
				.map(channelIndex -> new Channel(channelIndex, this.channelInstruments.get(channelIndex)))
				.collect(Collectors.toList());
	}

	public void addNotes(int measureValue, List<B00Note> notesList) {
		for (B00Note b00note : notesList) {
			Channel fromIndex = Channel.of(b00note.getChannel());
			if (!channelMap.containsKey(fromIndex)) {
				throw new RuntimeException("Missing channel " + fromIndex.toString());
			}
			Notes notes = channelMap.get(fromIndex);
			notes.add(measureValue, b00note.getAccent());
		}
	}

	public Pattern build() {
		int numberOfChannels = channelMap.size();

		Track[] trackpatterns = new Track[numberOfChannels];

		for (Entry<Channel, Notes> entry : channelMap.entrySet()) {
			trackpatterns[entry.getKey().getIndex()] = createTrackPattern(entry.getKey(), entry.getValue());
		}

		return new Pattern(patternIdent, quarterTime, trackpatterns);
	}

	private Track createTrackPattern(Channel channel, Notes notes) {
		Quantization quantization = determineQuantization(channel);

		Volume[] trackVolumes = new Volume[PatternConstants.TRACK_QUANTIZATION];

		notes.getNotes()
		.forEach(note -> trackVolumes[note.getMeasure()] = new Volume(note.getAccent()));

		Track track = new Track(channel.getIndex(), channel.getInstrument(), quantization);
		track.init(trackVolumes);
		return track;
	}

	private Quantization determineQuantization(Channel channel) {
		Notes notes = channelMap.get(channel);
		return Quantization.getMostCoarseQuantization(notes.getMeasures());
	}

}
