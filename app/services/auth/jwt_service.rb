class Auth::JwtService
  SECRET_KEY = Rails.application.credentials.devise_jwt_secret_key
  ALGORITHM = 'HS256'.freeze

  class << self
    def encode(payload)
      JWT.encode(payload, SECRET_KEY, ALGORITHM)
    end

    def decode(token)
      JWT.decode(token, SECRET_KEY, ALGORITHM)
    rescue JWT::DecodeError
      nil
    end
  end
end
