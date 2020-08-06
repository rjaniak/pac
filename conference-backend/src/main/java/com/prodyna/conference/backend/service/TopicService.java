package com.prodyna.conference.backend.service;

import com.prodyna.conference.backend.model.Topic;
import com.prodyna.conference.backend.model.TopicDTO;
import com.prodyna.conference.backend.repository.TopicRepository;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Ralf Janiak, PRODYNA SE
 */
@Service
@Transactional
@Timed
public class TopicService {

    @Autowired
    TopicRepository topicRepository;

    public Topic addTopic(TopicDTO topicDTO) {
        Topic topic = mapToTopic(topicDTO, new Topic());
        return topicRepository.save(topic);
    }

    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }

    public Topic getTopic(Long id) {
        Optional<Topic> topic = topicRepository.findById(id);
        if (!topic.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Topic found with id [" + id + "].");
        }
        return topic.get();
    }

    public List<Topic> getTopics() {
        List<Topic> topics = new ArrayList<>();
        Iterable<Topic> allTopics = topicRepository.findAll();
        allTopics.forEach(topics::add);
        return topics;
    }

    public Topic updateTopic(Long id, TopicDTO topicDTO) {
        Optional<Topic> topicOptional = topicRepository.findById(id);
        if (!topicOptional.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No Topic found with id [" + id + "].");
        }
        Topic topic = mapToTopic(topicDTO, topicOptional.get());
        return topicRepository.save(topic);
    }

    private Topic addParentTopics(Topic topic, List<Long> parentTopicIds) {
        Iterable<Topic> topics = topicRepository.findAllById(parentTopicIds);
        List<Topic> parentTopics = new ArrayList<>();
        topics.forEach(parentTopics::add);
        topic.setParentTopics(parentTopics);
        return topic;
    }

    private Topic mapToTopic(TopicDTO topicDTO, Topic topic) {
        topic.setName(topicDTO.getName());
        topic = addParentTopics(topic, topicDTO.getParentTopicIds());
        return topic;
    }
}
