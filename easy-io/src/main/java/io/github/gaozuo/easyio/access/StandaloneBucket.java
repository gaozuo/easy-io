package io.github.gaozuo.easyio.access;

import io.github.gaozuo.easyio.source.ObjectResource;
import io.github.gaozuo.easyio.source.PathResource;

import java.util.ArrayList;
import java.util.List;

public class StandaloneBucket implements ResourceBucket {

    private List<PathResource> pathResources = new ArrayList<>();
    private List<ObjectResource> objectResources = new ArrayList<>();
    private List<Throwable> errors = new ArrayList<>();

    public StandaloneBucket() {
    }

    public StandaloneBucket(List<PathResource> pathResources, List<ObjectResource> objectResources, List<Throwable> errors) {
        this.pathResources = pathResources == null ? new ArrayList<>() : pathResources;
        this.objectResources = objectResources == null ? new ArrayList<>() : objectResources;
        this.errors = errors == null ? new ArrayList<>() : errors;
    }

    public void addToPathResources(PathResource resource) {
        this.pathResources.add(resource);
    }

    public void addToObjectResources(ObjectResource resource) {
        this.objectResources.add(resource);
    }

    public void addToErrors(Throwable e) {
        this.errors.add(e);
    }

    @Override
    public boolean hasPathResource() {
        return !pathResources.isEmpty();
    }

    @Override
    public boolean hasObjectResource() {
        return !objectResources.isEmpty();
    }

    @Override
    public boolean hasError() {
        return !errors.isEmpty();
    }

    @Override
    public List<PathResource> listPathResource() {
        return new ArrayList<>(pathResources);
    }

    @Override
    public List<ObjectResource> listObjectResource() {
        return new ArrayList<>(objectResources);
    }

    @Override
    public List<Throwable> listError() {
        return new ArrayList<>(errors);
    }
}
