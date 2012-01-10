<table id="youtube_avaliable_sources" class="tablesorter">
	<thead>
		<tr>
			<th>Title</th>
			<th>Description</th>
			<th>User Rating</th>
			<th>Youtube ID</th>
			<th>Use this source</th>
		</tr>
	</thead>
	<tbody>
		$sources:{ source |
			<tr class="possible_source">
				<td class="title">$source.youtubeTitle$</td>
				<td class="description">$source.description$</td>
				<td class="feedbackRating">$source.feedbackRating$</td>
				<td class="youtubeId">$source.youtubeId$</td>
				<td><input type="submit" class="use_this_source_button mac_style_button" value="Use this source"></td>
			</tr>
		}$
	</tbody> 
</table>