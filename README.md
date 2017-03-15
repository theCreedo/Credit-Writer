## Inspiration
In the music industry, songwriters, composers, and music publishers may have their royalties ignored or delayed for months due to poor management and reporting. As of right now, only 30% of writers are actually credited, and even these writers have a delayed response in submissions of songs played, causing millions of dollars to be lost for over 90% of local artists. Especially with venues, it is very hard for hosts of a venue to gather the set list of 100s of artists they facilitate at a given concert or festival. With this very pressing problem, we've created Credit Writer, a monumental, user-friendly web app interface to battle against this pressing problem of lost credit.

## What it does
Our app, Credit Writer, provides venues an easy way to submit set lists. Using venue twitter hashtags run by listener's live tweet feeds, we help deliver musical writers the money they rightfully deserve. The app fills out a form for SOCAN, providing song titles, artists, and venue data, so that SOCAN can take steps to enforce the songwriter's rights.

## How we built it
Our website is built in HTML, CSS, Javascript, hosted on Github p ages. The Cloudfare library helped make everything beautiful and responsive. Coded in Java, we used the Twitter API to receive tweet data, parsing the data to produce song titles and artist names. Using MapQuest API, we produced a street address of venues from geolocation by tweets. Integrating Pubnub, we make real-time updates quickly and efficiently between two different machines. Lastly, we format JSON with all info to make a call to the SOCAN API, notifying them of a live performance.

## Challenges we ran into
Making a POST call using HTTP in Java was a new experience for some members. HTTPS calls required for the SOCAN API were difficult because of the SSL Certificates. Setting up programming environments was also painful.

## Accomplishments that we're proud of
- Setting up many buggy environmental variables like Apache Maven correctly.
- Integrating over 4 APIs together
- Getting real-time communication between servers and venues 

## What we learned
Together, we learned how to make ajax calls, troubleshoot APIS, and resolve issues between server data dependent communication.

## What's next for Credit Writer
In the future, we hope to format the data better to send more accurate set lists to SOCAN. Using noise reducing algorithms, we can easily remove data that isn't relevant to the venue's festivals/concerts

