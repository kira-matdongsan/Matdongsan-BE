class Paginator
  attr_reader :collection

  def initialize(collection)
    @collection = collection
  end

  def page(number = nil)
    @collection = @collection.page(number)
    self
  end

  def per(count = nil)
    @collection = @collection.per(count)
    self
  end

  def paginate(page: nil, per: nil)
    @collection = @collection.page(page).per(per)
    to_h
  end

  def to_h
    {
      contents: @collection,
      meta: {
        current_page: @collection.current_page,
        next_page: @collection.next_page,
        prev_page: @collection.prev_page,
        total_pages: @collection.total_pages,
        total_count: @collection.total_count
      }
    }
  end
end
