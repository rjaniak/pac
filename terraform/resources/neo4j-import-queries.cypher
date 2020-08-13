// Locations
LOAD CSV WITH HEADERS FROM "file:///locations.csv" AS row
CREATE (location:Location {locationId: row.id, name: row.name});

// Organizations
LOAD CSV WITH HEADERS FROM "file:///organizations.csv" AS row
CREATE (organization:Organization {organizationId: row.id, name: row.name});

// Events
LOAD CSV WITH HEADERS FROM "file:///events.csv" AS row
MATCH (location:Location {locationId: row.locationId})
CREATE (event:Event {eventId: row.id, name: row.name, begin: row.begin, end: row.end})
CREATE (event)-[:TAKES_PLACE_IN]->(location);

// Persons
LOAD CSV WITH HEADERS FROM "file:///persons.csv" AS row
MATCH (organization:Organization {organizationId: row.organizationId})
CREATE (person:Person {personId: row.id, name: row.name})
CREATE (person)-[:BELONGS_TO]->(organization);

// Rooms
LOAD CSV WITH HEADERS FROM "file:///rooms.csv" AS row
MATCH (location:Location {locationId: row.locationId})
CREATE (room:Room {roomId: row.id, name: row.name})
CREATE (room)-[:IS_IN]->(location);

// Topic nodes
LOAD CSV WITH HEADERS FROM "file:///topics.csv" AS row
CREATE (topic:Topic {topicId: row.id, name: row.name});

// Topic relationships (parentTopics)
LOAD CSV WITH HEADERS FROM "file:///topicToParentTopic.csv" AS row
MATCH (topic:Topic {topicId: row.childTopicId}),
(parentTopic:Topic {topicId: row.parentTopicId})
CREATE (topic)-[:RELATED_TO]->(parentTopic);

// TimeSlot nodes
LOAD CSV WITH HEADERS FROM "file:///timeSlots.csv" AS row
CREATE (timeSlot:TimeSlot {timeSlotId: row.id, date: row.date, startTime: row.startTime, endTime: row.endTime});

// TimeSlot relationships (blocked room)
LOAD CSV WITH HEADERS FROM "file:///timeSlotToRoom.csv" AS row
MATCH (timeSlot:TimeSlot {timeSlotId: row.timeSlotId}),
(room:Room {roomId: row.roomId})
CREATE (timeSlot)-[:BLOCKS]->(room);

// Talk nodes
LOAD CSV WITH HEADERS FROM "file:///talks.csv" AS row
CREATE (talk:Talk {talkId: row.id, title: row.title, date: row.date, startTime: row.startTime,
                   duration: toInteger(row.duration), language: row.language, level: row.level});

// Talk relationships (to event)
LOAD CSV WITH HEADERS FROM "file:///talkToEvent.csv" AS row
MATCH (talk:Talk {talkId: row.talkId}),
(event:Event {eventId: row.eventId})
CREATE (talk)-[:PART_OF]->(event);

// Talk relationships (to room)
LOAD CSV WITH HEADERS FROM "file:///talkToRoom.csv" AS row
MATCH (talk:Talk {talkId: row.talkId}),
(room:Room {roomId: row.roomId})
CREATE (talk)-[:HELD_IN]->(room);

// Talk relationships (to person)
LOAD CSV WITH HEADERS FROM "file:///talkToPerson.csv" AS row
MATCH (talk:Talk {talkId: row.talkId}),
(person:Person {personId: row.personId})
CREATE (talk)-[:HELD_BY]->(person);

// Talk relationships (to topic)
LOAD CSV WITH HEADERS FROM "file:///talkToTopic.csv" AS row
MATCH (talk:Talk {talkId: row.talkId}),
(topic:Topic {topicId: row.topicId})
CREATE (talk)-[:IS_ABOUT]->(topic);

// Talk relationships (to timeSlot)
LOAD CSV WITH HEADERS FROM "file:///talkToTimeSlot.csv" AS row
MATCH (talk:Talk {talkId: row.talkId}),
(timeSlot:TimeSlot {timeSlotId: row.timeSlotId})
CREATE (talk)-[:HAS]->(timeSlot);
